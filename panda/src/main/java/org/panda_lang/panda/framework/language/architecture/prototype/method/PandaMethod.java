/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.prototype.method;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;

public class PandaMethod implements PrototypeMethod {

    private final ClassPrototype prototype;
    private final String methodName;
    private final ClassPrototype[] parameterTypes;
    private final ClassPrototype returnType;
    private final MethodCallback methodBody;
    private MethodVisibility visibility;
    private final boolean isStatic;
    private final boolean catchAllParameters;

    protected PandaMethod(PandaMethodBuilder builder) {
        this.prototype = builder.prototype;
        this.methodName = builder.methodName;
        this.returnType = builder.returnType;
        this.methodBody = builder.methodBody;
        this.isStatic = builder.isStatic;
        this.visibility = builder.visibility;
        this.catchAllParameters = builder.catchAllParameters;
        this.parameterTypes = builder.parameterTypes != null ? builder.parameterTypes : new ClassPrototype[0];
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void invoke(ExecutableBranch branch, Object instance, Value... parameters) {
        methodBody.invoke(branch, instance, parameters);
    }

    @Override
    public boolean isCatchingAllParameters() {
        return catchAllParameters;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public MethodVisibility getVisibility() {
        return visibility;
    }

    @Override
    public ClassPrototype getReturnType() {
        return returnType;
    }

    @Override
    public ClassPrototype[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public ClassPrototype getClassPrototype() {
        return prototype;
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

}