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

package org.panda_lang.panda.language.structure.prototype.structure.constructor.variant;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.prototype.structure.ClassInstance;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.ClassScope;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.Constructor;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.ConstructorScope;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.ConstructorScopeInstance;
import org.panda_lang.panda.language.structure.prototype.structure.constructor.ConstructorUtils;
import org.panda_lang.panda.language.structure.prototype.structure.parameter.ParameterUtils;

public class PandaConstructor implements Constructor {

    private final ClassPrototype classPrototype;
    private final ClassScope classScope;
    private final ConstructorScope constructorScope;
    private final ClassPrototype[] types;

    public PandaConstructor(ClassPrototype classPrototype, ClassScope classScope, ConstructorScope constructorScope) {
        this.classPrototype = classPrototype;
        this.classScope = classScope;
        this.constructorScope = constructorScope;
        this.types = ConstructorUtils.toTypes(constructorScope.getParameters());
    }

    @Override
    public ClassInstance createInstance(ExecutableBranch bridge, Value... values) {
        ClassInstance classInstance = classScope.createInstance();
        ConstructorScopeInstance constructorInstance = constructorScope.createInstance();

        ParameterUtils.assignValues(constructorInstance.getVariables(), values);
        bridge.call(constructorInstance);

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
