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

package org.panda_lang.panda.framework.language.architecture.prototype.standard;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.utilities.commons.ClassUtils;

import java.util.Collection;
import java.util.Optional;

public class PandaClassPrototypeUtils {

    public static boolean isAssignableFrom(Class<?> from, Class<?> to) {
        return from != null && to != null && (from == to || ClassUtils.isAssignableFrom(from, to));
    }

    public static boolean hasCommonClasses(Collection<Class<?>> fromClasses, Collection<Class<?>> toClasses) {
        for (Class<?> from : fromClasses) {
            for (Class<?> to : toClasses) {
                if (from == to) {
                    return true;
                }

                if (isAssignableFrom(from, to)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasCommonPrototypes(Collection<? extends ClassPrototypeReference> fromPrototypes, Collection<? extends ClassPrototypeReference> toPrototypes) {
        for (ClassPrototypeReference from : fromPrototypes) {
            for (ClassPrototypeReference to : toPrototypes) {
                if (from.equals(to)) {
                    return true;
                }

                if (isAssignableFrom(from.getAssociatedClass(), to.getAssociatedClass())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ClassPrototype[] toTypes(ParserData data, ModuleLoader loader, Class<?>... types) {
        ClassPrototype[] prototypes = new ClassPrototype[types.length];

        for (int i = 0; i < types.length; i++) {
            Optional<ClassPrototypeReference> reference = loader.forClass(types[i]);

            if (reference.isPresent()) {
                prototypes[i] = reference.get().fetch();
                continue;
            }

            throw PandaParserFailure.builder("Unknown type " + types[i], data)
                    .withSourceFragment()
                        .ofOriginals(data)
                        .create()
                    .build();
        }

        return prototypes;
    }

}
