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

package org.panda_lang.panda.language.structure.prototype.structure.method.variant;

import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.PandaClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;

public class PandaMethodBuilder {

    private ClassPrototype prototype;
    private String methodName;
    private ClassPrototype[] parameterTypes;
    private MethodVisibility visibility;
    private ClassPrototype returnType;
    private MethodCallback methodBody;
    private boolean isStatic;

    public PandaMethodBuilder prototype(ClassPrototype prototype) {
        this.prototype = prototype;
        return this;
    }

    public PandaMethodBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public PandaMethodBuilder parameterTypes(String... parameterTypes) {
        ClassPrototype[] prototypes = new ClassPrototype[parameterTypes.length];

        for (int i = 0; i < prototypes.length; i++) {
            prototypes[i] = PandaClassPrototype.forName(parameterTypes[i]);
        }

        this.parameterTypes = prototypes;
        return this;
    }

    public PandaMethodBuilder returnType(ClassPrototype returnType) {
        this.returnType = returnType;
        return this;
    }

    public PandaMethodBuilder visibility(MethodVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public PandaMethodBuilder methodBody(MethodCallback callback) {
        this.methodBody = callback;
        return this;
    }

    public PandaMethodBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public PandaMethod build() {
        return new PandaMethod(prototype, visibility, isStatic, returnType, methodName, methodBody, parameterTypes);
    }

}
