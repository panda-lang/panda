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

package org.panda_lang.panda.implementation.syntax;

import org.panda_lang.framework.interpreter.token.suggestion.Keyword;

public class Keywords {

    public static final Keyword ABSTRACT = new Keyword("abstract");

    public static final Keyword AS = new Keyword("as");

    public static final Keyword BOOLEAN = new Keyword("boolean");

    public static final Keyword BREAK = new Keyword("break");

    public static final Keyword BYTE = new Keyword("byte");

    public static final Keyword CASE = new Keyword("catch");

    public static final Keyword CHAR = new Keyword("char");

    public static final Keyword CLASS = new Keyword("class");

    public static final Keyword CONSTRUCTOR = new Keyword("constructor");

    public static final Keyword CONTINUE = new Keyword("continue");

    public static final Keyword DOUBLE = new Keyword("double");

    public static final Keyword ELSE = new Keyword("else");

    public static final Keyword EXTENDS = new Keyword("extends");

    public static final Keyword FINAL = new Keyword("final");

    public static final Keyword FLOAT = new Keyword("float");

    public static final Keyword FOR = new Keyword("for");

    public static final Keyword GROUP = new Keyword("group");

    public static final Keyword IF = new Keyword("if");

    public static final Keyword IMPLEMENTS = new Keyword("implements");

    public static final Keyword INSTANCE_OF = new Keyword("instanceof");

    public static final Keyword INT = new Keyword("interface");

    public static final Keyword LONG = new Keyword("long");

    public static final Keyword MAIN = new Keyword("main");

    public static final Keyword METHOD = new Keyword("method");

    public static final Keyword NATIVE = new Keyword("native");

    public static final Keyword NEW = new Keyword("new");

    public static final Keyword RETURN = new Keyword("return");

    public static final Keyword REQUIRE = new Keyword("require");

    public static final Keyword SHORT = new Keyword("short");

    public static final Keyword STATIC = new Keyword("static");

    public static final Keyword SUPER = new Keyword("super");

    public static final Keyword SYNCHRONIZED = new Keyword("synchronized");

    public static final Keyword SWITCH = new Keyword("switch");

    public static final Keyword THIS = new Keyword("this");

    public static final Keyword WHILE = new Keyword("while");

    private static final Keyword[] VALUES = new Keyword[35];

    static {
        VALUES[0] = ABSTRACT;
        VALUES[1] = AS;
        VALUES[2] = BOOLEAN;
        VALUES[3] = BREAK;
        VALUES[4] = BYTE;
        VALUES[5] = CASE;
        VALUES[6] = CHAR;
        VALUES[7] = CLASS;
        VALUES[8] = CONSTRUCTOR;
        VALUES[9] = CONTINUE;
        VALUES[10] = DOUBLE;
        VALUES[11] = ELSE;
        VALUES[12] = EXTENDS;
        VALUES[13] = FINAL;
        VALUES[14] = FLOAT;
        VALUES[15] = FOR;
        VALUES[16] = GROUP;
        VALUES[17] = IF;
        VALUES[18] = IMPLEMENTS;
        VALUES[19] = INSTANCE_OF;
        VALUES[20] = INT;
        VALUES[21] = LONG;
        VALUES[22] = METHOD;
        VALUES[23] = NATIVE;
        VALUES[24] = NEW;
        VALUES[25] = RETURN;
        VALUES[26] = REQUIRE;
        VALUES[27] = SHORT;
        VALUES[28] = STATIC;
        VALUES[29] = SUPER;
        VALUES[30] = SYNCHRONIZED;
        VALUES[31] = SWITCH;
        VALUES[32] = THIS;
        VALUES[33] = WHILE;
        VALUES[34] = MAIN;
    }

    public static Keyword[] values() {
        return VALUES;
    }

}
