/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure;

import java.util.Collection;

public class PandaClassPrototypeUtils {

    public static boolean hasCommonClasses(Collection<Class<?>> a, Collection<Class<?>> b) {
        for (Class<?> aClass : a) {
            for (Class<?> bClass : b) {
                if (aClass == bClass) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasCommonPrototypes(Collection<ClassPrototype> a, Collection<ClassPrototype> b) {
        for (ClassPrototype aPrototype : a) {
            for (ClassPrototype bPrototype : b) {
                if (aPrototype.equals(bPrototype)) {
                    return true;
                }
            }
        }

        return false;
    }

}
