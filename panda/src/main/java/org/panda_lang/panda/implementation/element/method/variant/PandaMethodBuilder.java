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

import org.panda_lang.framework.structure.Executable;
import org.panda_lang.panda.implementation.element.method.Method;
import org.panda_lang.panda.implementation.element.method.MethodVisibility;

public class PandaMethodBuilder {

    private String methodName;
    private Executable methodBody;
    private boolean isStatic;
    private MethodVisibility visibility;


    public PandaMethodBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public PandaMethodBuilder methodBody(Executable executable) {
        this.methodBody = executable;
        return this;
    }

    public PandaMethodBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public PandaMethodBuilder visibility(MethodVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method build() {
        return new PandaMethod(methodName, methodBody, isStatic, visibility);
    }

}
