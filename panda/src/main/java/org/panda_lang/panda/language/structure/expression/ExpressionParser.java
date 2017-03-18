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

package org.panda_lang.panda.language.structure.expression;

import org.panda_lang.framework.interpreter.lexer.token.Token;
import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.implementation.structure.value.PandaValue;
import org.panda_lang.panda.language.structure.group.GroupRegistry;

public class ExpressionParser implements Parser {

    public Expression parse(ParserInfo info, TokenizedSource expressionSource) {
        if (expressionSource.size() == 1) {
            Token token = expressionSource.getToken(0);

            if (token.getType() == TokenType.LITERAL) {
                switch (token.getTokenValue()) {
                    case "null":
                        return new Expression(new PandaValue(GroupRegistry.forName("panda.lang:null"), null));
                    case "true":
                        return new Expression(null);
                    case "false":
                        return new Expression(null);
                }
            }
        }

        return null;
    }

}
