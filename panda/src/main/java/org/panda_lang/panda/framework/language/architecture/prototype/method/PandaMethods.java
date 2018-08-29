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
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaMethods implements PrototypeMethods {

    private final Map<String, Collection<PrototypeMethod>> methodsMap;

    public PandaMethods() {
        this.methodsMap = new HashMap<>();
    }

    @Override
    public void registerMethod(PrototypeMethod method) {
        Collection<PrototypeMethod> methods = methodsMap.computeIfAbsent(method.getMethodName(), methodsContainer -> new ArrayList<>());
        methods.add(method);
    }

    @Override
    public @Nullable PrototypeMethod getMethod(String name, ClassPrototype... parameterTypes) {
        Collection<PrototypeMethod> methods = methodsMap.get(name);

        if (methods == null) {
            return null;
        }

        return MethodUtils.matchMethod(methods, parameterTypes);
    }

    @Override
    public String toString() {
        return "PrototypeMethods[" + methodsMap.size() + "]";
    }

}
