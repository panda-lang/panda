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

package org.panda_lang.language.interpreter.parser.stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.panda_lang.utilities.commons.collection.Lists.add;

public final class Phases {

    private static final Collection<Phase> VALUES = new ArrayList<>();

    public static final Phase SYNTAX = add(VALUES, new Phase("SYNTAX", 1.0));

    public static final Phase PREPROCESSOR = add(VALUES, new Phase("PREPROCESSOR", 2.0));

    public static final Phase TYPES = add(VALUES, new Phase("TYPES", 2.0));

    public static final Phase DEFAULT = add(VALUES, new Phase("DEFAULT", 3.0));

    public static final Phase CONTENT = add(VALUES, new Phase("CONTENT", 4.0));

    public static final Phase VERIFY = add(VALUES, new Phase("VERIFY", 5.0));

    public static final Phase INITIALIZE = add(VALUES, new Phase("INITIALIZE", 6.0));

    private Phases() { }

    public static List<? extends Phase> getValues() {
        return new ArrayList<>(VALUES);
    }

}
