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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression.fixed;

import org.junit.jupiter.api.Test;

class ExpressionParserTest extends ExpressionParserTestBootstrap {

    @Test
    public void parseUnknown() {
        parse("u n k n o w n", "Cannot parse the expression: Cannot find variable or field called 'u'");
    }

    @Test
    public void parseSequences() {
        parse("'hello panda'");
        parse("'hello panda' 'hello expressions'", "Source contains 2 expressions");
    }

    @Test
    public void parseLiterals() {
        parse("null");
        parse("true false",  RuntimeException.class, "Unread source: false");
    }

    @Test
    public void parseSection() {
        parse("('chance')");
        parse("()", "Cannot parse the expression: Expression expected");
        parse("('random') true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseNumber() {
        parse("10");
        parse("10.0");
        parse("10.0F");
        parse("10_000");
        parse("10.0F true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseVariable() {
        parse("variable");
        parse("variable true", RuntimeException.class, "Unread source: true");
        parse("variable.field", "Cannot parse the expression, the latest error: Cannot find field called 'field'");
    }

    @Test
    public void parseArrayAssignation() {
        parse("array[0]");
        parse("array[]", "Cannot parse the expression, the latest error: Expression expected");
        parse("array[0] true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseOperation() {
        parse("1 + 1");
    }

}
