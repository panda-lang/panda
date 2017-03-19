/*
 * Copyright (c) 2015-2017 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.language.structure.group;

import org.panda_lang.framework.interpreter.lexer.token.Token;
import org.panda_lang.framework.interpreter.lexer.token.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.PandaScript;
import org.panda_lang.panda.implementation.structure.Script;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.language.structure.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.parser.ClassPrototypeReference;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = GroupParser.class, handlerClass = GroupParserHandler.class)
public class GroupParser implements UnifiedParser {

    private static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "group")
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new GroupDeclarationParserCallback(), parserInfo)
                .delegateAfter(new GroupAfterParserCallback(), parserInfo.fork());
    }

    @LocalCallback
    private static class GroupDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenizedSource hollow = hollows.getGap(0);

            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String groupName = groupNameBuilder.toString();

            GroupRegistry registry = GroupRegistry.getDefault();
            Group group = registry.getOrCreate(groupName);

            GroupStatement groupStatement = new GroupStatement(group);
            script.getStatements().add(groupStatement);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(group);
        }

    }

    @LocalCallback
    private static class GroupAfterParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Script script = delegatedInfo.getComponent(Components.SCRIPT);

            List<GroupStatement> groupStatements = script.select(GroupStatement.class);
            List<ClassPrototypeReference> prototypeReferences = script.select(ClassPrototypeReference.class);

            if (groupStatements.size() == 0) {
                return;
            }

            if (groupStatements.size() > 1) {
                throw new PandaParserException("Script contains more than one declaration of the group");
            }

            Group group = groupStatements.get(0).getGroup();

            for (ClassPrototypeReference prototypeReference : prototypeReferences) {
                ClassPrototype classPrototype = prototypeReference.getClassPrototype();

                group.add(classPrototype);
                classPrototype.getGroup().setObject(group);
            }
        }

    }

}
