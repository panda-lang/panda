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

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.parser.ClassPrototypeReference;

import java.util.List;

@ParserRegistration(parserClass = GroupParser.class, handlerClass = GroupParserHandler.class)
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
                .delegateImmediately(new ParserGenerationCallback() {
                    @Override
                    public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
                        Script script = delegatedInfo.getComponent(Components.SCRIPT);

                        TokenPatternHollows gaps = TokenPatternUtils.extract(PATTERN, delegatedInfo);
                        String groupName = gaps.getToken(0, 0).getTokenValue();

                        GroupRegistry registry = GroupRegistry.getDefault();
                        Group group = registry.getOrCreate(groupName);

                        GroupStatement groupStatement = new GroupStatement(group);
                        script.getStatements().add(groupStatement);
                    }
                }, parserInfo)
                .delegateAfter(new ParserGenerationCallback() {
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
                }, parserInfo.clone());
    }

}
