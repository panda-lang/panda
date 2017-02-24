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

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPatternGaps;
import org.panda_lang.panda.implementation.interpreter.lexer.token.extractor.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.ParserRegistration;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

@ParserRegistration(parserClass = ClassPrototypeParser.class, handlerClass = ClassPrototypeParserHandler.class)
public class ClassPrototypeParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "class")
            .gap()
            .unit(TokenType.SEPARATOR, "{")
            .gap()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ParserGenerationCallback() {
                    @Override
                    public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
                        Script script = delegatedInfo.getComponent(Components.SCRIPT);
                        TokenPatternGaps gaps = TokenPatternUtils.extract(PATTERN, delegatedInfo);

                        String className = gaps.getToken(0, 0).getTokenValue();
                        ClassPrototype classPrototype = new ClassPrototype(className);

                        ClassPrototypeReference prototypeReference = new ClassPrototypeReference(classPrototype);
                        script.getStatements().add(prototypeReference);
                    }
                }, parserInfo)
                .delegateAfter(new ParserGenerationCallback() {
                    @Override
                    public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {

                    }
                }, parserInfo.clone());
    }

}
