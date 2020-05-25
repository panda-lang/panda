/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.architecture.type;

import io.vavr.control.Option;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Adjustment;
import org.panda_lang.framework.design.architecture.type.Methods;
import org.panda_lang.framework.design.architecture.type.State;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.language.architecture.type.utils.ParameterUtils;

import java.util.List;

final class PandaMethods extends AbstractProperties<TypeMethod> implements Methods {

    private static final TypeExecutablePropertiesMatcher<TypeMethod> MATCHER = new TypeExecutablePropertiesMatcher<>();

    PandaMethods(Type type) {
        super(TypeMethod.class, type);
    }

    @Override
    public Option<TypeMethod> getMethod(String name, Type[] parameterTypes) {
        return MATCHER.match(getPropertiesLike(name), parameterTypes, null).map(Adjustment::getExecutable);
    }

    @Override
    public Option<Adjustment<TypeMethod>> getAdjustedArguments(String name, Expression[] arguments) {
        return MATCHER.match(getPropertiesLike(name), ParameterUtils.toTypes(arguments), arguments);
    }

    @Override
    public List<? extends TypeMethod> getPropertiesLike(String name) {
        return super.getPropertiesLike(name, method -> super.type.getState() == State.ABSTRACT || !method.isAbstract());
    }

    @Override
    public List<? extends TypeMethod> getProperties() {
        return super.getProperties(method -> super.type.getState() == State.ABSTRACT || !method.isAbstract());
    }

    @Override
    public List<? extends TypeMethod> getDeclaredProperties() {
        return super.getDeclaredProperties(method -> super.type.getState() == State.ABSTRACT || !method.isAbstract());
    }

    @Override
    public String toString() {
        return "Methods[" + size() + "]";
    }

}
