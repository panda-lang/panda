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

import org.panda_lang.framework.design.architecture.prototype.ExecutableProperty;
import org.panda_lang.framework.design.architecture.prototype.PropertyParameter;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Referencable;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.design.architecture.prototype.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;

import java.util.Arrays;
import java.util.List;

abstract class AbstractExecutableProperty extends AbstractProperty implements ExecutableProperty {

    private final Prototype prototype;
    private final PropertyParameter[] parameters;
    private final Prototype returnType;
    private final PrototypeExecutableCallback callback;

    protected AbstractExecutableProperty(PandaParametrizedExecutableBuilder builder) {
        super(builder.name, builder.location, builder.visibility);

        this.prototype = builder.prototype;
        this.returnType = builder.returnType.fetch();
        this.parameters = builder.parameters;
        this.callback = builder.callback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return callback.invoke(stack, instance, arguments);
    }

    @Override
    public Prototype[] getParameterTypes() {
        return Arrays.stream(getParameters())
                .map(PropertyParameter::getType)
                .toArray(Prototype[]::new);
    }

    @Override
    public PropertyParameter[] getParameters() {
        return parameters;
    }

    @Override
    public Prototype getType() {
        return returnType;
    }

    @Override
    public Prototype getPrototype() {
        return prototype;
    }

    public abstract static class PandaParametrizedExecutableBuilder<T extends PandaParametrizedExecutableBuilder> {

        protected String name;
        protected Reference returnType;
        protected Prototype prototype;
        protected SourceLocation location;
        protected PrototypeExecutableCallback callback;
        protected Visibility visibility = Visibility.PUBLIC;
        protected PropertyParameter[] parameters = ParameterUtils.PARAMETERLESS;

        public T type(Prototype prototype) {
            prototype(prototype).returnType(prototype.toReference());
            return returnThis();
        }

        public T prototype(Prototype prototype) {
            this.prototype = prototype;
            return returnThis();
        }

        public T name(String methodName) {
            this.name = methodName;
            return returnThis();
        }

        public T location(SourceLocation location) {
            this.location = location;
            return returnThis();
        }

        public T parameters(PropertyParameter... parameters) {
            this.parameters = parameters;
            return returnThis();
        }

        public T parameters(List<PropertyParameter> parameters) {
            this.parameters = parameters.toArray(new PropertyParameter[0]);
            return returnThis();
        }

        public T returnType(Referencable returnType) {
            this.returnType = returnType.toReference();
            return returnThis();
        }

        public T visibility(Visibility visibility) {
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
