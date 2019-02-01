/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.token.distributors;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

import java.util.HashMap;
import java.util.Map;

public class DistributorUtils {

    public static final Map<Token, Token> REVERSED_OPERATORS = new HashMap<Token, Token>() {{
        put(Separators.BRACE_LEFT, Separators.BRACE_RIGHT);
        put(Separators.PARENTHESIS_LEFT, Separators.PARENTHESIS_RIGHT);
        put(Separators.SQUARE_BRACKET_LEFT, Separators.SQUARE_BRACKET_RIGHT);
        //put(Separators.ANGLE_BRACKET_LEFT, Separators.SQUARE_BRACKET_RIGHT);

        put(Separators.BRACE_RIGHT, Separators.BRACE_LEFT);
        put(Separators.PARENTHESIS_RIGHT, Separators.PARENTHESIS_LEFT);
        put(Separators.SQUARE_BRACKET_RIGHT, Separators.SQUARE_BRACKET_LEFT);
        //put(Separators.ANGLE_BRACKET_RIGHT, Separators.SQUARE_BRACKET_LEFT);
    }};

}
