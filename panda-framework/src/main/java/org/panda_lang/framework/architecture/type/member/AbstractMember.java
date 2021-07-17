/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.type.member;

import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.Visibility;
import org.panda_lang.framework.architecture.type.member.parameter.PropertyParameter;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.interpreter.source.Localizable;
import org.panda_lang.framework.interpreter.source.Location;
import panda.utilities.ObjectUtils;

import java.util.Collections;
import java.util.List;

public abstract class AbstractMember<E extends Member> extends AbstractMetadata implements Member {

    private final Type type;
    private final Signature returnType;

    protected AbstractMember(PandaParametrizedExecutableBuilder<E, ?> builder) {
        super(builder.name, builder.location, builder.visibility, builder.isNative);

        this.type = builder.type;
        this.returnType = builder.returnType;
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
        protected MemberInvoker<E, Object, Object> invoker;
        protected Visibility visibility = Visibility.OPEN;
        protected List<? extends PropertyParameter> parameters = Collections.emptyList();
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

        public T parameters(List<? extends PropertyParameter> parameters) {
            this.parameters = parameters;
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

        public T invoker(MemberInvoker<E, ?, ?> invoker) {
            this.invoker = ObjectUtils.cast(invoker);
            return returnThis();
        }

        @SuppressWarnings("unchecked")
        private T returnThis() {
            return (T) this;
        }

    }

}
