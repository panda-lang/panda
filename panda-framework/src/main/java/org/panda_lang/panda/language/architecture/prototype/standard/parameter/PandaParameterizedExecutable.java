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

package org.panda_lang.panda.language.architecture.prototype.standard.parameter;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.PrototypeVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.ParameterizedExecutable;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;

import java.util.Arrays;
import java.util.List;

public abstract class PandaParameterizedExecutable implements ParameterizedExecutable {

    private final String name;
    private final PrototypeParameter[] parameters;
    private final ClassPrototypeReference returnType;
    private final PrototypeVisibility visibility;
    private final ClassPrototypeReference prototype;
    private final ParametrizedExecutableCallback callback;

    protected PandaParameterizedExecutable(PandaParametrizedExecutableBuilder builder) {
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
    public ClassPrototypeReference getPrototype() {
        return prototype;
    }

    @Override
    public PrototypeVisibility getVisibility() {
        return visibility;
    }

    @Override
    public ClassPrototypeReference[] getParameterTypes() {
        return Arrays.stream(getParameters())
                .map(PrototypeParameter::getType)
                .toArray(ClassPrototypeReference[]::new);
    }

    @Override
    public PrototypeParameter[] getParameters() {
        return parameters;
    }

    @Override
    public ClassPrototypeReference getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    public abstract static class PandaParametrizedExecutableBuilder<T extends PandaParametrizedExecutableBuilder> {

        protected String name;
        protected ClassPrototypeReference returnType;
        protected ClassPrototypeReference prototype;
        protected ParametrizedExecutableCallback callback;
        protected PrototypeVisibility visibility = PrototypeVisibility.PUBLIC;
        protected PrototypeParameter[] parameters = ParameterUtils.PARAMETERLESS;

        public T type(ClassPrototypeReference reference) {
            prototype(reference).returnType(reference);
            return returnThis();
        }

        public T prototype(ClassPrototypeReference reference) {
            this.prototype = reference;
            return returnThis();
        }

        public T name(String methodName) {
            this.name = methodName;
            return returnThis();
        }

        public T parameters(PrototypeParameter... parameters) {
            this.parameters = parameters;
            return returnThis();
        }

        public T parameters(List<PrototypeParameter> parameters) {
            this.parameters = parameters.toArray(new PrototypeParameter[0]);
            return returnThis();
        }

        public T returnType(ClassPrototypeReference returnType) {
            this.returnType = returnType;
            return returnThis();
        }

        public T visibility(PrototypeVisibility visibility) {
            this.visibility = visibility;
            return returnThis();
        }

        public T callback(ParametrizedExecutableCallback callback) {
            this.callback = callback;
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        private T returnThis() {
            return (T) this;
        }

    }
}
