/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.resource.syntax.keyword;

import java.util.ArrayList;
import java.util.Collection;

import static org.panda_lang.utilities.commons.collection.Lists.add;

/**
 * Default keywords
 */
public final class Keywords {

    private static final Collection<Keyword> VALUES = new ArrayList<>();

    public static final Keyword AS = add(VALUES, new Keyword("as"));

    public static final Keyword BASE = add(VALUES, new Keyword("base"));

    public static final Keyword BREAK = add(VALUES, new Keyword("break"));

    public static final Keyword CASE = add(VALUES, new Keyword("case"));

    public static final Keyword CATCH = add(VALUES, new Keyword("catch"));

    public static final Keyword CLASS = add(VALUES, new Keyword("class"));

    public static final Keyword CONSTRUCTOR = add(VALUES, new Keyword("constructor"));

    public static final Keyword CONTINUE = add(VALUES, new Keyword("continue"));

    public static final Keyword ELSE = add(VALUES, new Keyword("else"));

    public static final Keyword EXPORT = add(VALUES, new Keyword("export"));

    public static final Keyword FOREACH = add(VALUES, new Keyword("foreach"));

    public static final Keyword FOR = add(VALUES, new Keyword("for"));

    public static final Keyword IF = add(VALUES, new Keyword("if"));

    public static final Keyword IMPORT = add(VALUES, new Keyword("import"));

    public static final Keyword INTERFACE = add(VALUES, new Keyword("interface"));

    public static final Keyword INTERNAL = add(VALUES, new Keyword("internal"));

    public static final Keyword IS = add(VALUES, new Keyword("is"));

    public static final Keyword LATE = add(VALUES, new Keyword("late"));

    public static final Keyword LAZY = add(VALUES, new Keyword("lazy"));

    public static final Keyword LOG = add(VALUES, new Keyword("log"));

    public static final Keyword LOOP = add(VALUES, new Keyword("loop"));

    public static final Keyword MAIN = add(VALUES, new Keyword("main"));

    public static final Keyword MODULE = add(VALUES, new Keyword("module"));

    public static final Keyword MUT = add(VALUES, new Keyword("mut"));

    public static final Keyword NEW = add(VALUES, new Keyword("new"));

    public static final Keyword NIL = add(VALUES, new Keyword("nil"));

    public static final Keyword NOT = add(VALUES, new Keyword("not"));

    public static final Keyword OPEN = add(VALUES, new Keyword("open"));

    public static final Keyword OVERRIDE = add(VALUES, new Keyword("override"));

    public static final Keyword PUBLIC = add(VALUES, new Keyword("public"));

    public static final Keyword RETURN = add(VALUES, new Keyword("return"));

    public static final Keyword REQUIRE = add(VALUES, new Keyword("require"));

    public static final Keyword SELF = add(VALUES, new Keyword("self"));

    public static final Keyword SHARED = add(VALUES, new Keyword("shared"));

    public static final Keyword STATIC = add(VALUES, new Keyword("static"));

    public static final Keyword SWITCH = add(VALUES, new Keyword("switch"));

    public static final Keyword THROW = add(VALUES, new Keyword("throw"));

    public static final Keyword TRY = add(VALUES, new Keyword("try"));

    public static final Keyword TYPE = add(VALUES, new Keyword("type"));

    public static final Keyword WHILE = add(VALUES, new Keyword("while"));

    private Keywords() { }

    public static Keyword[] values() {
        return VALUES.toArray(new Keyword[0]);
    }

}
