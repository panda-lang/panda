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

import java.util.Optional;

final class PandaConstructors extends AbstractProperties<PrototypeConstructor> implements Constructors {

    private static final PrototypeExecutablePropertiesMatcher<PrototypeConstructor> MATCHER = new PrototypeExecutablePropertiesMatcher<>();

    PandaConstructors(Prototype prototype) {
        super(PrototypeConstructor.class, prototype);
    }

    @Override
    public Optional<PrototypeConstructor> getConstructor(Prototype[] types) {
        return MATCHER.match(getDeclaredProperties(), types, null).map(Adjustment::getExecutable);
    }

    @Override
    public Optional<Adjustment<PrototypeConstructor>> getAdjustedConstructor(Expression[] arguments) {
        return MATCHER.match(getDeclaredProperties(), ParameterUtils.toTypes(arguments), arguments);
    }

}
