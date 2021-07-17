/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.resource.syntax.literal;

import java.util.ArrayList;
import java.util.Collection;

import static panda.utilities.collection.Lists.add;

/**
 * Default literals
 */
public final class Literals {

    private static final Collection<Literal> VALUES = new ArrayList<>();

    public static final Literal FALSE = add(VALUES, new Literal("false"));

    public static final Literal TRUE = add(VALUES, new Literal("true"));

    public static final Literal THIS = add(VALUES, new Literal("this"));

    private Literals() { }

    public static Literal[] values() {
        return VALUES.toArray(new Literal[0]);
    }

}
