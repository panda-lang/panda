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

import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Value;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.language.structure.prototype.ClassInstance;

public class PandaMethod implements Method {

    private final String methodName;
    private final Executable methodBody;
    private final boolean isStatic;
    private MethodVisibility visibility;

    public PandaMethod(String methodName, Executable methodBody, boolean isStatic, MethodVisibility visibility) {
        this.methodName = methodName;
        this.methodBody = methodBody;
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    @Override
    public Value invoke(ClassInstance instance, Value... parameters) {
        return null;
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
    public String getName() {
        return methodName;
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

}