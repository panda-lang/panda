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

package org.panda_lang.language.architecture.type.member;

import org.panda_lang.language.architecture.type.signature.Signature;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.language.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractMember<E extends Member> extends AbstractMetadata implements Member {

    private final Type type;
    private final PropertyParameter[] parameters;
    private final Signature returnType;
    private final MemberInvoker<E, Object> callback;

    protected AbstractMember(PandaParametrizedExecutableBuilder<E, ?> builder) {
        super(builder.name, builder.location, builder.visibility, builder.isNative);

        this.type = builder.type;
        this.returnType = builder.returnType;
        this.parameters = builder.parameters;
        this.callback = builder.callback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return callback.invoke((E) this, stack, instance, arguments);
    }

    @Override
    public Signature[] getParameterSignatures() {
        return Arrays.stream(getParameters())
                .map(PropertyParameter::getSignature)
                .toArray(Signature[]::new);
    }

    @Override
    public PropertyParameter[] getParameters() {
        return parameters;
    }

    @Override
    public Signature getReturnType() {
        return returnType;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName();
    }

    public abstract static class PandaParametrizedExecutableBuilder<E extends Member, T extends PandaParametrizedExecutableBuilder<E, ?>> {

        protected String name;
        protected Type type;
        protected Signature returnType;
        protected Location location;
        protected MemberInvoker<E, Object> callback;
        protected Visibility visibility = Visibility.OPEN;
        protected PropertyParameter[] parameters = ParameterUtils.PARAMETERLESS;
        protected boolean isNative;

        public T type(Type type) {
            this.type = type;
            return returnThis();
        }

        public T name(String methodName) {
            this.name = methodName;
            return returnThis();
        }

        public T location(Localizable localizable) {
            this.location = localizable.toLocation();
            return returnThis();
        }

        public T parameters(PropertyParameter... parameters) {
            this.parameters = parameters;
            return returnThis();
        }

        public T parameters(List<? extends PropertyParameter> parameters) {
            this.parameters = parameters.toArray(new PropertyParameter[0]);
            return returnThis();
        }

        public T returnType(Signature returnType) {
            this.returnType = returnType;
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

        public T callback(MemberInvoker<E, Object> callback) {
            this.callback = callback;
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        private T returnThis() {
            return (T) this;
        }

    }

}
