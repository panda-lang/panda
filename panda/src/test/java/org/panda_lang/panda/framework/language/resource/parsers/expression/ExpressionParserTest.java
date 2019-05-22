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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.junit.jupiter.api.Test;

class ExpressionParserTest extends ExpressionParserTestBootstrap {

    @Test
    public void parseUnknown() {
        parse("u n k n o w n", "Unknown expression");
    }

    @Test
    public void parseSequences() {
        parse("'hello panda'");
        parse("'hello panda' 'hello expressions'", "Source contains 2 expressions");
    }

    @Test
    public void parseLiterals() {
        parse("null");
        parse("true false", RuntimeException.class, "Unread source: false");
    }

    @Test
    public void parseSection() {
        parse("('chance')");
        parse("()", "Expression expected");
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
        parse("variable.field", "Cannot find field called 'field'");
        parse("String.CASE_INSENSITIVE_ORDER");
    }

    @Test
    public void parseArrayAssignation() {
        parse("array[0]");
        parse("array[]", "Expression expected");
        parse("array['text']", "Index of array has to be Integer");
        parse("array[0] true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseOperation() {
        parse("1 + 1");
        parse("(5 + 2) * 4");
        parse("'a' + 'b'");
        parse("'a' == 'b'");
        parse("1 < 2");
    }

    @Test
    public void parseMethod() {
        parse("variable.toString()");
        parse("variable.toString().toString()");
        parse("variable.equals(1 + 2)");
        parse("variable.toString() true", RuntimeException.class, "Unread source: true");
        parse("String.valueOf(variable)");
    }

    @Test
    public void parseNegate() {
        parse("!true");
        parse("!false && !false");
        parse("!true false", RuntimeException.class, "Unread source: false");
    }

    @Test
    public void parseConstructor() {
        parse("new StringBuilder()");
        parse("new StringBuilder('a')");
        parse("new StringBuilder() true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseArrayConstructor() {
        parse("new String[0]");
        parse("new String[]", "Array requires specified capacity");
        parse("new String['a']", "Capacity has to be Int");
        parse("new String[0] true", RuntimeException.class, "Unread source: true");
    }

    @Test
    public void parseCrease() {
        parse("i++");
        parse("++i");
        parse("i--");
        parse("--i");
    }

}
