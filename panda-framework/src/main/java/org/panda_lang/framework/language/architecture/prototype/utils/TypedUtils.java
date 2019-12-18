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

package org.panda_lang.framework.language.architecture.prototype.utils;

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Typed;
import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.Arrays;
import java.util.Collection;

public final class TypedUtils {

    private TypedUtils() { }

    public static String toString(Typed... typed) {
        return toString(Arrays.asList(typed));
    }

    public static String toString(Collection<? extends Typed> typed) {
        return ContentJoiner.on(", ")
                .join(typed.stream()
                    .map(Typed::getType)
                    .map(Prototype::getSimpleName)
                    .toArray())
                .toString();
    }

}
