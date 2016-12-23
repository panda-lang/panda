/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.panda.implementation.element.method.variant;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Executable;
import org.panda_lang.panda.implementation.element.method.Method;
import org.panda_lang.panda.implementation.element.method.MethodVisibility;

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
    public void execute(ExecutableBridge executionInfo) {

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
    public Executable getBody() {
        return methodBody;
    }

    @Override
    public String getName() {
        return methodName;
    }

    public static PandaMethodBuilder builder() {
        return new PandaMethodBuilder();
    }

}