/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package org.panda_lang.panda.language.resource.expression;

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Test;

@CompileStatic
final class ExpressionParserTest extends ExpressionParserTestBootstrap {

    @Test
    void parseUnknown() {
        // parse("u n k n o w n", "Unknown expression 'u n k n o w n'") TODO: better generics handling
    }

    @Test
    void parseSequences() {
        parse("'hello panda'")
        parse("'hello panda' 'hello expressions'", RuntimeException.class, "Unread source: 'hello expressions'")
    }

    @Test
    void parseLiterals() {
        parse("true false", RuntimeException.class, "Unread source: false")
    }

    @Test
    void parseSection() {
        parse("('chance')")
        parse("()", "Expression expected")
        parse("('random') true", RuntimeException.class, "Unread source: true")
    }

    @Test
    void parseNumber() {
        parse("10")
        parse("10.0")
        parse("10.0F")
        parse("10_000")
        parse("10.0F true", RuntimeException.class, "Unread source: true")
    }

    @Test
    void parseVariable() {
        parse("variable")
        parse("variable true", RuntimeException.class, "Unread source: true")
        parse("variable.field", "Cannot find field called 'field'")
        parse("String.CASE_INSENSITIVE_ORDER")
    }

    /*
    @Test
    public void parseArrayAssignation() {
        parse("array[0]")
        parse("array[]", "Missing index expression")
        parse("array['text']", "Index of array has to be Integer")
        parse("array[0] true", RuntimeException.class, "Unread source: true")
    }
     */

    @Test
    void parseOperation() {
        parse("1 + 1")
        parse("(5 + 2) * 4")
        parse("'a' + 'b'")
        parse("'a' == 'b'")
        parse("1 < 2")
    }

    @Test
    void parseMethod() {
        parse("variable.toString()")
        parse("variable.toString().toString()")
        parse("variable.equals(1 + 2)")
        parse("variable.toString() true", RuntimeException.class, "Unread source: true")
        parse("String.valueOf(variable)")
    }

    @Test
    void parseNegate() {
        parse("!true")
        // parse("!false && !false") TODO: negation has to be improved due to bug
        parse("!true false", RuntimeException.class, "Unread source: false")
    }

    @Test
    public void parseConstructor() {
        parse("new StringBuilder()")
        parse("new StringBuilder('a')")
        parse("new StringBuilder() true", RuntimeException.class, "Unread source: true")
    }

    /*
    @Test
    public void parseArrayConstructor() {
        parse("new String[0]")
        parse("new String[]", "Array requires specified capacity")
        parse("new String['a']", "Capacity has to be Int")
        parse("new String[0] true", RuntimeException.class, "Unread source: true")
    }
     */

    @Test
    void parseCrease() {
        parse("i++")
        parse("++i")
        parse("i--")
        parse("--i")
    }

}
