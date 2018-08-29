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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression;

import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

public class ExpressionPatterns {

    protected static final AbyssPattern FIELD_PATTERN = new AbyssPatternBuilder()
            .hollow()
            .unit(Separators.PERIOD)
            .simpleHollow()
            .build();

    protected static final AbyssPattern INSTANCE_PATTERN = new AbyssPatternBuilder()
            .unit(TokenType.KEYWORD, "new")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, ")")
            .simpleHollow()
            .build();

}
