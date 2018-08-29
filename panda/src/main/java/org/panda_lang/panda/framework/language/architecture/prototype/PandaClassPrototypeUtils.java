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

package org.panda_lang.panda.framework.language.architecture.prototype;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;

import java.util.Collection;

public class PandaClassPrototypeUtils {

    public static boolean isAssociatedWith(Class<?> a, Class<?> b) {
        return a != null && b != null && (a == b || b.isAssignableFrom(a));
    }

    public static boolean hasCommonClasses(Collection<Class<?>> fromClasses, Collection<Class<?>> toClasses) {
        for (Class<?> a : fromClasses) {
            for (Class<?> b : toClasses) {
                if (a == b) {
                    return true;
                }

                if (isAssociatedWith(a, b)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasCommonPrototypes(Collection<ClassPrototype> fromPrototypes, Collection<ClassPrototype> toPrototypes) {
        for (ClassPrototype a : fromPrototypes) {
            for (ClassPrototype b : toPrototypes) {
                if (a.equals(b)) {
                    return true;
                }

                if (isAssociatedWith(a.getAssociated(), b.getAssociated())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ClassPrototype[] toTypes(ModuleLoader loader, Class<?>... types) {
        ClassPrototype[] prototypes = new ClassPrototype[types.length];

        for (int i = 0; i < types.length; i++) {
            prototypes[i] = loader.forClass(types[i]);
        }

        return prototypes;
    }

}
