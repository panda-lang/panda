/*
 * Copyright (c) 2015-2020 Dzikoysk
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

import org.panda_lang.framework.design.architecture.type.ExecutableProperty;
import org.panda_lang.framework.design.architecture.type.PropertyParameter;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.Referencable;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.design.architecture.type.Visibility;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.type.utils.ParameterUtils;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractExecutableProperty extends AbstractProperty implements ExecutableProperty {

    private final Type type;
    private final PropertyParameter[] parameters;
    private final Type returnType;
    private final TypeExecutableCallback callback;

    protected AbstractExecutableProperty(PandaParametrizedExecutableBuilder<?> builder) {
        super(builder.name, builder.location, builder.visibility, builder.isNative);

        this.type = builder.type;
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
    public Type[] getParameterTypes() {
        return Arrays.stream(getParameters())
                .map(PropertyParameter::getType)
                .toArray(Type[]::new);
    }

    @Override
    public PropertyParameter[] getParameters() {
        return parameters;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public Type getType() {
        return type;
    }

    public abstract static class PandaParametrizedExecutableBuilder<T extends PandaParametrizedExecutableBuilder<?>> {

        protected String name;
        protected Reference returnType;
        protected Type type;
        protected SourceLocation location;
        protected TypeExecutableCallback<?> callback;
        protected Visibility visibility = Visibility.PUBLIC;
        protected PropertyParameter[] parameters = ParameterUtils.PARAMETERLESS;
        protected boolean isNative;

        public T type(Type type) {
            this.type = type;

            if (returnType == null) {
                this.returnType = type.toReference();
            }

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

        public T isNative(boolean isNative) {
            this.isNative = isNative;
            return returnThis();
        }

        public T callback(TypeExecutableCallback<?> callback) {
            this.callback = callback;
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        private T returnThis() {
            return (T) this;
        }

    }

}
