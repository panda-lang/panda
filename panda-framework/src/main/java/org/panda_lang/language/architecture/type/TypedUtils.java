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

package org.panda_lang.language.architecture.type;

import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public final class TypedUtils {

    private TypedUtils() { }

    private static Stream<Type> toTypesStream(Collection<? extends Signed> typed) {
        return typed.stream().map(Signed::getType);
    }

    public static Type[] toTypes(Signed... signed) {
        return toTypes(Arrays.asList(signed));
    }

    public static Type[] toTypes(Collection<? extends Signed> typed) {
        return toTypesStream(typed).toArray(Type[]::new);
    }

    public static Class<?>[] toClasses(Signed... signed) {
        return toClasses(Arrays.asList(signed));
    }

    public static Class<?>[] toClasses(Collection<? extends Signed> typed) {
        return toTypesStream(typed)
                .map(type -> type.getAssociatedClass().fetchStructure())
                .toArray(Class[]::new);
    }

    public static String toString(Signed... signed) {
        return toString(Arrays.asList(signed));
    }

    public static String toString(Collection<? extends Signed> typed) {
        return ContentJoiner.on(", ")
                .join(toTypesStream(typed)
                    .map(Type::getSimpleName)
                    .toArray())
                .toString();
    }

}
