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

package org.panda_lang.panda.framework.language.architecture.prototype.method;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;

import java.util.Collection;

public class MethodUtils {

    public static @Nullable PrototypeMethod matchMethod(Collection<PrototypeMethod> methods, ClassPrototype... types) {
        for (PrototypeMethod method : methods) {
            ClassPrototype[] methodTypes = method.getParameterTypes();

            if (method.isCatchingAllParameters()) {
                return method;
            }

            if (methodTypes.length != types.length) {
                continue;
            }

            if (matchParameters(method, types)) {
                return method;
            }
        }

        return null;
    }

    public static boolean matchParameters(PrototypeMethod method, ClassPrototype... parameterTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            ClassPrototype methodParameterType = method.getParameterTypes()[i];
            ClassPrototype parameterType = parameterTypes[i];

            if (!parameterType.isAssociatedWith(methodParameterType)) {
                return false;
            }
        }

        return true;
    }

}
