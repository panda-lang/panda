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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.constructor;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructors;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.Arguments;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParametrizedPropertiesMatcher;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PandaConstructors implements PrototypeConstructors {

    private static final ParametrizedPropertiesMatcher<PrototypeConstructor> MATCHER = new ParametrizedPropertiesMatcher<>();

    private final List<PrototypeConstructor> constructors = new ArrayList<>(1);

    @Override
    public void declare(PrototypeConstructor constructor) {
        constructors.add(constructor);
    }

    @Override
    public Optional<PrototypeConstructor> getConstructor(ClassPrototype[] types) {
        return MATCHER.match(constructors, types, null).map(Arguments::getExecutable);
    }

    @Override
    public Optional<Arguments<PrototypeConstructor>> getAdjustedConstructor(Expression[] arguments) {
        return MATCHER.match(constructors, ExpressionUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<? extends PrototypeConstructor> getProperties() {
        return constructors;
    }

    @Override
    public int size() {
        return constructors.size();
    }

}
