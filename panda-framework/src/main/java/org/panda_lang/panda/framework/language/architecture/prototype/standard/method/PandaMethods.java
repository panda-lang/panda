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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.method;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethods;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Arguments;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParametrizedPropertiesMatcher;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PandaMethods implements PrototypeMethods {

    private static final ParametrizedPropertiesMatcher<PrototypeMethod> MATCHER = new ParametrizedPropertiesMatcher<>();

    private final Map<String, Collection<PrototypeMethod>> methodsMap;

    public PandaMethods() {
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
    public Optional<PrototypeMethod> getMethod(String name, ClassPrototype... parameterTypes) {
        Collection<PrototypeMethod> methods = methodsMap.get(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, parameterTypes, null).map(Arguments::getExecutable);
    }

    @Override
    public Optional<Arguments<PrototypeMethod>> getAdjustedArguments(String name, Expression[] arguments) {
        Collection<PrototypeMethod> methods = methodsMap.get(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, ExpressionUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<? extends PrototypeMethod> getProperties() {
        return methodsMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "PrototypeMethods[" + methodsMap.size() + "]";
    }

}
