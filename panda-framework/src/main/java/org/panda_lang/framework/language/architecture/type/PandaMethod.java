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

import org.panda_lang.framework.design.architecture.type.TypeMethod;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.type.utils.ParameterUtils;
import org.panda_lang.utilities.commons.ObjectUtils;

public final class PandaMethod extends AbstractExecutableProperty<TypeMethod> implements TypeMethod {

    private final TypeExecutableCallback<TypeMethod, Object> methodBody;
    private final boolean isAbstract;
    private final boolean isStatic;
    private final boolean isNative;

    protected PandaMethod(PandaMethodBuilder builder) {
        super(builder);
        this.methodBody = builder.body;
        this.isAbstract = builder.isAbstract;
        this.isNative = builder.isNative;
        this.isStatic = builder.isStatic;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Object invoke(ProcessStack stack, Object instance, Object... parameters) throws Exception {
        return methodBody.invoke(this, stack, instance, parameters);
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
    public String getName() {
        return getType() + "#" + getSimpleName() + "(" + ParameterUtils.toString(getParameters()) + ") → " + getReturnType();
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

    public static final class PandaMethodBuilder extends PandaParametrizedExecutableBuilder<TypeMethod, PandaMethodBuilder> {

        protected TypeExecutableCallback<TypeMethod, Object> body;
        protected boolean isAbstract;
        protected boolean isStatic;

        private PandaMethodBuilder() { }

        public PandaMethodBuilder body(MethodScope scope) {
            return customBody(ObjectUtils.cast(scope.toCallback()));
        }

        public PandaMethodBuilder customBody(TypeExecutableCallback<TypeMethod, Object> callback) {
            this.body = callback;
            return this;
        }

        public PandaMethodBuilder isAbstract(boolean isAbstract) {
            this.isAbstract = isAbstract;
            return this;
        }

        public PandaMethodBuilder isStatic(boolean isStatic) {
            this.isStatic = isStatic;
            return this;
        }

        public PandaMethod build() {
            return new PandaMethod(this);
        }

    }

}