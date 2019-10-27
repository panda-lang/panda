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
import org.panda_lang.framework.design.architecture.prototype.Constructors;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class PandaConstructors extends AbstractProperties<PrototypeConstructor> implements Constructors {

    private static final PrototypeExecutablePropertiesMatcher<PrototypeConstructor> MATCHER = new PrototypeExecutablePropertiesMatcher<>();

    private final List<PrototypeConstructor> constructors = new ArrayList<>(1);

    PandaConstructors(Prototype prototype) {
        super(prototype);
    }

    @Override
    public void declare(PrototypeConstructor constructor) {
        constructors.add(constructor);
    }

    @Override
    public Optional<PrototypeConstructor> getConstructor(Prototype[] types) {
        return MATCHER.match(constructors, types, null).map(Adjustment::getExecutable);
    }

    @Override
    public Optional<Adjustment<PrototypeConstructor>> getAdjustedConstructor(Expression[] arguments) {
        return MATCHER.match(constructors, ParameterUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<PrototypeConstructor> getDeclaredProperties() {
        return constructors;
    }

    @Override
    public List<? extends PrototypeConstructor> getProperties() {
        List<PrototypeConstructor> constructors = getDeclaredProperties();
        super.getPrototype().getBases().forEach(base -> constructors.addAll(base.getConstructors().getProperties()));
        return constructors;
    }

    @Override
    public int size() {
        return constructors.size();
    }

}
