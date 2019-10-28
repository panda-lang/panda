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

import java.util.Collection;
import java.util.Optional;

final class PandaMethods extends AbstractProperties<PrototypeMethod> implements Methods {

    private static final PrototypeExecutablePropertiesMatcher<PrototypeMethod> MATCHER = new PrototypeExecutablePropertiesMatcher<>();


    PandaMethods(Prototype prototype) {
        super(PrototypeMethod.class, prototype);
    }

    @Override
    public Optional<PrototypeMethod> getMethod(String name, Referencable... parameterTypes) {
        Collection<? extends PrototypeMethod> methods = getPropertiesLike(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, parameterTypes, null).map(Adjustment::getExecutable);
    }

    @Override
    public Optional<Adjustment<PrototypeMethod>> getAdjustedArguments(String name, Expression[] arguments) {
        Collection<? extends PrototypeMethod> methods = getPropertiesLike(name);

        if (methods == null) {
            return Optional.empty();
        }

        return MATCHER.match(methods, ParameterUtils.toTypes(arguments), arguments);
    }

    @Override
    public String toString() {
        return "PrototypeMethods[" + size() + "]";
    }

}
