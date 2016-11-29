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

package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.structure.Executable;

public class MethodBuilder {

    private String methodName;
    private Executable methodBody;
    private boolean isStatic;
    private MethodVisibility visibility;


    public MethodBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public MethodBuilder methodBody(Executable executable) {
        this.methodBody = executable;
        return this;
    }

    public MethodBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public MethodBuilder visibility(MethodVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method build() {
        return new Method(methodName, methodBody, isStatic, visibility);
    }

}
