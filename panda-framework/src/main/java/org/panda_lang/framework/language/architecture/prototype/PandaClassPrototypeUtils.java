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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.utilities.commons.ClassUtils;

import java.util.Collection;
import java.util.Optional;

public final class PandaClassPrototypeUtils {

    private PandaClassPrototypeUtils() { }

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

    public static boolean hasCommonPrototypes(Collection<? extends PrototypeReference> fromPrototypes, Collection<? extends PrototypeReference> toPrototypes) {
        for (PrototypeReference from : fromPrototypes) {
            for (PrototypeReference to : toPrototypes) {
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

    public static Prototype[] toTypes(Context context, ModuleLoader loader, Class<?>... types) {
        Prototype[] prototypes = new Prototype[types.length];

        for (int i = 0; i < types.length; i++) {
            Optional<PrototypeReference> reference = loader.forName(types[i].getCanonicalName());

            if (reference.isPresent()) {
                prototypes[i] = reference.get().fetch();
                continue;
            }

            throw PandaParserFailure.builder("Unknown type " + types[i], context)
                    .withSourceFragment()
                        .ofOriginals(context)
                        .create()
                    .build();
        }

        return prototypes;
    }

}
