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

package org.panda_lang.panda.framework.language.architecture.prototype.constructor;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.constructor.PrototypeConstructor;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScope;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScopeInstance;
import org.panda_lang.panda.framework.language.architecture.prototype.parameter.ParameterUtils;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;

public class PandaConstructor implements PrototypeConstructor {

    private final ClassPrototype classPrototype;
    private final ClassScope classScope;
    private final ConstructorScope constructorScope;
    private final ClassPrototype[] types;

    public PandaConstructor(ClassPrototype classPrototype, ClassScope classScope, ConstructorScope constructorScope) {
        this.classPrototype = classPrototype;
        this.classScope = classScope;
        this.constructorScope = constructorScope;
        this.types = ParameterUtils.toTypes(constructorScope.getParameters());
    }

    @Override
    public ClassScopeInstance createInstance(ExecutableBranch branch, Value... values) {
        ClassScopeInstance classInstance = classScope.createInstance(branch);
        Value instance = new PandaValue(classPrototype, classInstance);

        ConstructorScopeInstance constructorInstance = constructorScope.createInstance(branch);
        ParameterUtils.assignValues(constructorInstance, values);

        branch.instance(instance);
        branch.call(constructorInstance);

        return classInstance;
    }

    @Override
    public ClassPrototype[] getParameterTypes() {
        return types;
    }

    public ClassPrototype getClassPrototype() {
        return classPrototype;
    }

}
