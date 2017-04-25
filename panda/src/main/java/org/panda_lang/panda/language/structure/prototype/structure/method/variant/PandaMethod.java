/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.method.variant;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;

public class PandaMethod implements Method {

    private final ClassPrototype prototype;
    private final String methodName;
    private final ClassPrototype[] parameterTypes;
    private final ClassPrototype returnType;
    private final MethodCallback methodBody;
    private final boolean isStatic;
    private MethodVisibility visibility;

    public PandaMethod(ClassPrototype prototype, MethodVisibility visibility, boolean isStatic, ClassPrototype returnType, String methodName, MethodCallback methodBody,  ClassPrototype... parameterTypes) {
        this.prototype = prototype;
        this.methodName = methodName;
        this.returnType = returnType;
        this.methodBody = methodBody;
        this.isStatic = isStatic;
        this.visibility = visibility;
        this.parameterTypes = parameterTypes != null ? parameterTypes : new ClassPrototype[0];
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void invoke(ExecutableBranch bridge, Object instance, Value... parameters) {
        methodBody.invoke(bridge, instance, parameters);
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