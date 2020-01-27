/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.prototype.utils;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Typed;
import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public final class TypedUtils {

    private TypedUtils() { }

    private static Stream<Prototype> toPrototypes(Collection<? extends Typed> typed) {
        return typed.stream().map(Typed::getType);
    }

    public static Prototype[] toTypes(Typed... typed) {
        return toTypes(Arrays.asList(typed));
    }

    public static Prototype[] toTypes(Collection<? extends Typed> typed) {
        return toPrototypes(typed).toArray(Prototype[]::new);
    }

    public static Class<?>[] toClasses(Typed... typed) {
        return toClasses(Arrays.asList(typed));
    }

    public static Class<?>[] toClasses(Collection<? extends Typed> typed) {
        return toPrototypes(typed)
                .map(prototype -> prototype.getAssociatedClass().fetchImplementation())
                .toArray(Class[]::new);
    }

    public static String toString(Typed... typed) {
        return toString(Arrays.asList(typed));
    }

    public static String toString(Collection<? extends Typed> typed) {
        return ContentJoiner.on(", ")
                .join(toPrototypes(typed)
                    .map(Prototype::getSimpleName)
                    .toArray())
                .toString();
    }

}
