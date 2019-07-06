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

package org.panda_lang.panda.framework.language.architecture.prototype.standard.method;

import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.ParametrizedExecutableCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.parameter.PandaParameterizedExecutable;
import org.panda_lang.panda.framework.language.resource.PandaTypes;

public class PandaMethod extends PandaParameterizedExecutable implements PrototypeMethod {

    private final ParametrizedExecutableCallback methodBody;
    private final boolean isStatic;

    protected PandaMethod(PandaMethodBuilder builder) {
        super(builder);
        this.methodBody = builder.methodBody;
        this.isStatic = builder.isStatic;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Value invoke(Frame frame, Object instance, Value... parameters) throws Exception {
        return methodBody.invoke(frame, instance, parameters);
    }

    @Override
    public boolean isVoid() {
        return PandaTypes.VOID.isAssignableFrom(getReturnType());
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

    public static class PandaMethodBuilder extends PandaParameterizedExecutable.PandaParametrizedExecutableBuilder<PandaMethodBuilder> {

        protected ParametrizedExecutableCallback methodBody;
        protected boolean isStatic;

        private PandaMethodBuilder() { }

        public PandaMethodBuilder methodBody(ParametrizedExecutableCallback callback) {
            this.methodBody = callback;
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