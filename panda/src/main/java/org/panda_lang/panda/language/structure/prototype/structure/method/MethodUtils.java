/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.method;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

import java.util.Collection;

public class MethodUtils {

    public static Method matchMethod(Collection<Method> methods, ClassPrototype... types) {
        MATCHER:
        for (Method method : methods) {
            ClassPrototype[] methodTypes = method.getParameterTypes();

            if (methodTypes.length != types.length) {
                continue;
            }

            for (int i = 0; i < methodTypes.length; i++) {
                ClassPrototype methodType = methodTypes[i];
                ClassPrototype type = types[i];

                if (!methodType.equals(type)) {
                    continue MATCHER;
                }
            }

            return method;
        }

        return null;
    }

}
