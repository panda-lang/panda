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

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Adjustment;
import org.panda_lang.framework.design.architecture.prototype.Methods;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.architecture.prototype.Referencable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

final class PandaMethods extends AbstractProperties<PrototypeMethod> implements Methods {

    private static final PrototypeExecutablePropertiesMatcher<PrototypeMethod> MATCHER = new PrototypeExecutablePropertiesMatcher<>();

    private final Map<String, Collection<PrototypeMethod>> methodsMap;

    PandaMethods(Prototype prototype) {
        super(prototype);
        this.methodsMap = new HashMap<>();
    }

    @Override
    public void declare(PrototypeMethod method) {
        Collection<PrototypeMethod> methods = methodsMap.computeIfAbsent(method.getName(), methodsContainer -> new ArrayList<>());
        methods.add(method);
    }

    @Override
    public boolean hasMethodLike(String name) {
        return methodsMap.containsKey(name);
    }

    @Override
    public Collection<PrototypeMethod> getMethodsLike(String name) {
        return methodsMap.getOrDefault(name, Collections.emptyList());
    }

    @Override
    public Optional<PrototypeMethod> getMethod(String name, Referencable... parameterTypes) {
        Collection<PrototypeMethod> methods = methodsMap.get(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, parameterTypes, null).map(Adjustment::getExecutable);
    }

    @Override
    public Optional<Adjustment<PrototypeMethod>> getAdjustedArguments(String name, Expression[] arguments) {
        Collection<PrototypeMethod> methods = methodsMap.get(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, ParameterUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<PrototypeMethod> getDeclaredProperties() {
        return methodsMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends PrototypeMethod> getProperties() {
        List<PrototypeMethod> methods = getDeclaredProperties();
        super.getPrototype().getBases().forEach(base -> methods.addAll(base.getMethods().getProperties()));
        return methods;
    }

    @Override
    public int size() {
        return methodsMap.values().stream()
                .mapToInt(Collection::size)
                .sum();
    }

    @Override
    public String toString() {
        return "PrototypeMethods[" + methodsMap.size() + "]";
    }

}
