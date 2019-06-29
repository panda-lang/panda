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

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodCallback;
import org.panda_lang.panda.framework.design.architecture.prototype.method.MethodVisibility;

public class PandaMethodBuilder {

    protected String methodName;
    protected ClassPrototypeReference reference;
    protected ClassPrototypeReference[] parameterTypes;
    protected ClassPrototypeReference returnType;
    protected MethodVisibility visibility;
    protected MethodCallback methodBody;
    protected boolean isStatic;
    protected boolean catchAllParameters;

    public PandaMethodBuilder prototype(ClassPrototypeReference reference) {
        this.reference = reference;
        return this;
    }

    public PandaMethodBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /*
    public PandaMethodBuilder parameterTypes(ParserData context, ModuleLoader moduleLoader, String... parameterTypes) {
        ClassPrototypeReference[] prototypes = new ClassPrototypeReference[parameterTypes.length];

        for (int i = 0; i < prototypes.length; i++) {
            Optional<ClassPrototypeReference> reference = moduleLoader.forClass(parameterTypes[i]);

            if (!reference.isPresent()) {
                throw new PandaParserFailure("Unknown type " + parameterTypes[i], context);
            }

            prototypes[i] = reference.get();
        }

        this.parameterTypes = prototypes;
        return this;
    }
    */

    public PandaMethodBuilder parameterTypes(ClassPrototypeReference... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public PandaMethodBuilder returnType(ClassPrototypeReference returnType) {
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

    public PandaMethodBuilder catchAllParameters(boolean catchAllParameters) {
        this.catchAllParameters = catchAllParameters;
        return this;
    }

    public PandaMethod build() {
        return new PandaMethod(this);
    }

    public PandaMethodBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

}
