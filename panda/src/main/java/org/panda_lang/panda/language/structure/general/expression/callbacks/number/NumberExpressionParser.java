/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.general.expression.callbacks.number;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;

public class NumberExpressionParser implements Parser {

    private Value value;

    public void parse(TokenizedSource source, ParserInfo info) {
        /*
        0x001
        10B
        10.0
        4.2D
         */

        System.out.println("xx");
    }

    public static boolean isNumeric(TokenizedSource source) {
        for (TokenRepresentation tokenRepresentation : source.getTokensRepresentations()) {
            if (!TokenUtils.isNumber(tokenRepresentation.getToken())) {
                return false;
            }
        }

        return true;
    }

    public Value getValue() {
        return value;
    }

}
