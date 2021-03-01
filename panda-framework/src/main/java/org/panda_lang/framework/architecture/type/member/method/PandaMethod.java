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

package org.panda_lang.framework.architecture.type.member.method;

import org.panda_lang.framework.architecture.type.member.AbstractParametrizedMember;
import org.panda_lang.framework.architecture.type.member.MemberInvoker;
import org.panda_lang.framework.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.framework.runtime.ProcessStack;
import org.panda_lang.utilities.commons.ObjectUtils;

public final class PandaMethod extends AbstractParametrizedMember<TypeMethod> implements TypeMethod {

    private final MemberInvoker<TypeMethod, Object, Object> methodBody;
    private final boolean isAbstract;
    private final boolean isStatic;
    private final boolean isNative;
    private final boolean isOverriding;

    protected PandaMethod(PandaMethodBuilder builder) {
        super(builder);

        this.methodBody = builder.body;
        this.isAbstract = builder.isAbstract;
        this.isNative = builder.isNative;
        this.isStatic = builder.isStatic;
        this.isOverriding = builder.isOverriding;
    }

    @Override
    public Object invoke(ProcessStack stack, Object instance, Object... arguments) throws Exception {
        return methodBody.invoke(this, stack, instance, arguments);
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isOverriding() {
        return isOverriding;
    }

    @Override
    public String getName() {
        return getType() + "#" + getSimpleName() + "(" + ParameterUtils.toString(getParameters()) + ") → " + getReturnType();
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

    public static final class PandaMethodBuilder extends PandaParametrizedExecutableBuilder<TypeMethod, PandaMethodBuilder> {

        protected MemberInvoker<TypeMethod, Object, Object> body;
        protected boolean isAbstract;
        protected boolean isStatic;
        protected boolean isNative;
        protected boolean isOverriding;

        private PandaMethodBuilder() { }

        public PandaMethodBuilder body(MethodScope scope) {
            return customBody(scope);
        }

        public PandaMethodBuilder customBody(MemberInvoker<TypeMethod, ?, ?> invoker) {
            this.body = ObjectUtils.cast(invoker);
            return this;
        }

        public PandaMethodBuilder isAbstract(boolean isAbstract) {
            this.isAbstract = isAbstract;
            return this;
        }

        public PandaMethodBuilder isOverriding(boolean isOverriding) {
            this.isOverriding = isOverriding;
            return this;
        }

        public PandaMethodBuilder isStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public PandaMethodBuilder isNative(boolean isNative) {
            this.isNative = isNative;
            return this;
        }

        public PandaMethod build() {
            return new PandaMethod(this);
        }

    }

}