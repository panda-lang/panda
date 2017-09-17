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

package org.panda_lang.panda.language.structure.overall.module;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.core.structure.Script;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;

import java.util.Collection;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ModuleParser.class, handlerClass = ModuleParserHandler.class)
public class ModuleParser implements UnifiedParser {

    private static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "module")
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new GroupDeclarationCasualParserCallback(), info)
                .delegateAfter(new GroupAfterCasualParserCallback(), info.fork());
    }

    @LocalCallback
    private static class GroupDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenizedSource hollow = hollows.getGap(0);

            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String groupName = groupNameBuilder.toString();

            ModuleRegistry registry = ModuleRegistry.getDefault();
            Module module = registry.getOrCreate(groupName);
            script.setModule(module);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(module);

            ModuleStatement moduleStatement = new ModuleStatement(module);
            script.getStatements().add(moduleStatement);
        }

    }

    @LocalCallback
    private static class GroupAfterCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            Script script = delegatedInfo.getComponent(Components.SCRIPT);
            Collection<ModuleStatement> moduleStatements = script.select(ModuleStatement.class);

            if (moduleStatements.size() == 0) {
                return;
            }

            if (moduleStatements.size() > 1) {
                throw new PandaParserException("Script contains more than one declaration of the group");
            }
        }

    }

}
