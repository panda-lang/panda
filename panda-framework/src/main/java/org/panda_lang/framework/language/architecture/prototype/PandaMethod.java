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

import org.panda_lang.framework.design.architecture.prototype.PrototypeMethod;
import org.panda_lang.framework.design.runtime.ProcessStack;

public class PandaMethod extends AbstractExecutableProperty implements PrototypeMethod {

    private final PrototypeExecutableCallback methodBody;
    private final boolean isAbstract;
    private final boolean isStatic;

    protected PandaMethod(PandaMethodBuilder builder) {
        super(builder);
        this.methodBody = builder.methodBody;
        this.isAbstract = builder.isAbstract;
        this.isStatic = builder.isStatic;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Object invoke(ProcessStack stack, Object instance, Object... parameters) throws Exception {
        return methodBody.invoke(stack, instance, parameters);
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        return "method " + getReference().getName() + "#" + getName();
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

    public static class PandaMethodBuilder extends AbstractExecutableProperty.PandaParametrizedExecutableBuilder<PandaMethodBuilder> {

        protected PrototypeExecutableCallback methodBody;
        protected boolean isAbstract;
        protected boolean isStatic;

        private PandaMethodBuilder() { }

        public PandaMethodBuilder methodBody(MethodScope scope) {
            return methodBody(scope.toCallback());
        }

        public PandaMethodBuilder methodBody(PrototypeExecutableCallback callback) {
            this.methodBody = callback;
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