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

package org.panda_lang.panda.framework.language.resource.syntax.keyword;

import org.panda_lang.panda.utilities.commons.ReflectionUtils;

import java.util.Collection;

/**
 * Default keywords
 */
public class Keywords {

    public static final Keyword ABSTRACT = new Keyword("abstract");

    public static final Keyword AS = new Keyword("as");

    public static final Keyword ATTACH = new Keyword("attach");

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

    public static final Keyword FOREACH = new Keyword("foreach");

    public static final Keyword FOR = new Keyword("for");

    public static final Keyword GROUP = new Keyword("group");

    public static final Keyword HIDDEN = new Keyword("hidden");

    public static final Keyword IF = new Keyword("if");

    public static final Keyword IMPLEMENTS = new Keyword("implements");

    public static final Keyword IMPORT = new Keyword("import");

    public static final Keyword INSTANCE_OF = new Keyword("instanceof");

    public static final Keyword INT = new Keyword("int");

    public static final Keyword INTERFACE = new Keyword("interface");

    public static final Keyword LOCAL = new Keyword("local");

    public static final Keyword LONG = new Keyword("long");

    public static final Keyword LOOP = new Keyword("loop");

    public static final Keyword MAIN = new Keyword("main");

    public static final Keyword METHOD = new Keyword("method");

    public static final Keyword MODULE = new Keyword("module");

    public static final Keyword MUTABLE = new Keyword("mutable");

    public static final Keyword NATIVE = new Keyword("native");

    public static final Keyword NEW = new Keyword("new");

    public static final Keyword NULLABLE = new Keyword("nullable");

    public static final Keyword PRIVATE = new Keyword("private");

    public static final Keyword PUBLIC = new Keyword("public");

    public static final Keyword RETURN = new Keyword("return");

    public static final Keyword REQUIRE = new Keyword("require");

    public static final Keyword SHORT = new Keyword("short");

    public static final Keyword STATIC = new Keyword("static");

    public static final Keyword SUPER = new Keyword("super");

    public static final Keyword SYNCHRONIZED = new Keyword("synchronized");

    public static final Keyword SWITCH = new Keyword("switch");

    public static final Keyword WHILE = new Keyword("while");

    private static final Collection<Keyword> VALUES;

    static {
        VALUES = ReflectionUtils.getStaticFieldValues(Keywords.class, Keyword.class);
    }

    public static Keyword[] values() {
        return VALUES.toArray(new Keyword[0]);
    }

}
