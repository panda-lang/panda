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

package org.panda_lang.panda.language.structure.prototype.parser;

import org.panda_lang.framework.interpreter.lexer.token.Token;
import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.Script;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "class")
            .hollow()
            .unit(TokenType.SEPARATOR, "{")
            .hollow()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ClassPrototypeExtractorCallback(), parserInfo.fork());
    }

    @LocalCallback
    private static class ClassPrototypeExtractorCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Script script = delegatedInfo.getComponent(Components.SCRIPT);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("class-declaration", "class-body");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource classDeclaration = redactor.get("class-declaration");
            String className = classDeclaration.getToken(0).getTokenValue();

            ClassPrototype classPrototype = new ClassPrototype(className);
            delegatedInfo.setComponent("class", classPrototype);

            if (classDeclaration.size() > 1) {
                nextLayer.delegate(new ClassPrototypeDeclarationParserCallback(), delegatedInfo);
            }
            nextLayer.delegate(new ClassPrototypeBodyParserCallback(), delegatedInfo);

            ClassPrototypeReference prototypeReference = new ClassPrototypeReference(classPrototype);
            script.getStatements().add(prototypeReference);
        }

    }

    @LocalCallback
    private static class ClassPrototypeDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            ClassPrototype classPrototype = delegatedInfo.getComponent("class");

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource classDeclaration = redactor.get("class-declaration");
            Token next = classDeclaration.getToken(1);

            if (next.getType() != TokenType.KEYWORD) {
                throw new PandaParserException("Unknown element " + next);
            }

            switch (next.getTokenValue()) {
                case "extends":
                    for (int i = 2; i < classDeclaration.size(); i++) {
                        Token classNameToken = classDeclaration.getToken(i);

                        if (classNameToken.getType() == TokenType.UNKNOWN) {
                            ClassPrototype extendedPrototype = classPrototype.getGroup().getObject().get(classNameToken.getTokenValue());

                            if (extendedPrototype == null) {
                                throw new PandaParserException("Class " + classNameToken.getTokenValue() + " not found");
                            }

                            classPrototype.getExtended().add(extendedPrototype);
                            continue;
                        }
                        else if (classNameToken.getType() == TokenType.SEPARATOR) {
                            continue;
                        }

                        break;
                    }
                    break;
                default:
                    throw new PandaParserException("Illegal keyword " + next);
            }
        }

    }

    @LocalCallback
    private static class ClassPrototypeBodyParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {

        }

    }

}
