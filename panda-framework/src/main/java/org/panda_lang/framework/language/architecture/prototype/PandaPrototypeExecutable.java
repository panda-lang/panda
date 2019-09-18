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

import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.prototype.PrototypeVisibility;
import org.panda_lang.framework.design.architecture.prototype.PrototypeExecutable;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.parameter.ParameterUtils;

import java.util.Arrays;
import java.util.List;

public abstract class PandaPrototypeExecutable implements PrototypeExecutable {

    private final String name;
    private final Parameter[] parameters;
    private final PrototypeReference returnType;
    private final PrototypeVisibility visibility;
    private final PrototypeReference prototype;
    private final PrototypeExecutableCallback callback;

    protected PandaPrototypeExecutable(PandaParametrizedExecutableBuilder builder) {
        this.name = builder.name;
        this.parameters = builder.parameters;
        this.returnType = builder.returnType;
        this.visibility = builder.visibility;
        this.prototype = builder.prototype;
        this.callback = builder.callback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return callback.invoke(stack, instance, arguments);
    }

    @Override
    public PrototypeReference getPrototype() {
        return prototype;
    }

    @Override
    public PrototypeVisibility getVisibility() {
        return visibility;
    }

    @Override
    public PrototypeReference[] getParameterTypes() {
        return Arrays.stream(getParameters())
                .map(Parameter::getType)
                .toArray(PrototypeReference[]::new);
    }

    @Override
    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public PrototypeReference getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    public abstract static class PandaParametrizedExecutableBuilder<T extends PandaParametrizedExecutableBuilder> {

        protected String name;
        protected PrototypeReference returnType;
        protected PrototypeReference prototype;
        protected PrototypeExecutableCallback callback;
        protected PrototypeVisibility visibility = PrototypeVisibility.PUBLIC;
        protected Parameter[] parameters = ParameterUtils.PARAMETERLESS;

        public T type(PrototypeReference reference) {
            prototype(reference).returnType(reference);
            return returnThis();
        }

        public T prototype(PrototypeReference reference) {
            this.prototype = reference;
            return returnThis();
        }

        public T name(String methodName) {
            this.name = methodName;
            return returnThis();
        }

        public T parameters(Parameter... parameters) {
            this.parameters = parameters;
            return returnThis();
        }

        public T parameters(List<Parameter> parameters) {
            this.parameters = parameters.toArray(new Parameter[0]);
            return returnThis();
        }

        public T returnType(PrototypeReference returnType) {
            this.returnType = returnType;
            return returnThis();
        }

        public T visibility(PrototypeVisibility visibility) {
            this.visibility = visibility;
            return returnThis();
        }

        public T callback(PrototypeExecutableCallback callback) {
            this.callback = callback;
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        private T returnThis() {
            return (T) this;
        }

    }
}
