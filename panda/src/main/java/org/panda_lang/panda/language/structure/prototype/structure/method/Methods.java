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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Methods {

    private final ClassPrototype prototype;
    private final Map<String, Collection<Method>> methodsMap;

    public Methods(ClassPrototype prototype) {
        this.prototype = prototype;
        this.methodsMap = new HashMap<>();
    }

    public void registerMethod(Method method) {
        Collection<Method> methods = methodsMap.computeIfAbsent(method.getMethodName(), methodsContainer -> new ArrayList<>());
        methods.add(method);
    }

    public Method getMethod(String name, ClassPrototype... parameterTypes) {
        Collection<Method> methods = methodsMap.get(name);

        if (methods == null) {
            return null;
        }

        return MethodUtils.matchMethod(methods, parameterTypes);
    }

}
