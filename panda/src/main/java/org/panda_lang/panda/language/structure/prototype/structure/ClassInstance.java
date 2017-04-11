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

package org.panda_lang.panda.language.structure.prototype.structure;

import org.panda_lang.panda.core.structure.dynamic.ScopeInstance;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;

public class ClassInstance implements ScopeInstance {

    private final ClassScope scope;
    private final ClassPrototype prototype;
    private final Value[] fieldValues;

    public ClassInstance(ClassScope scope, ClassPrototype classPrototype) {
        this.scope = scope;
        this.prototype = classPrototype;
        this.fieldValues = new Value[classPrototype.getFields().size()];
    }

    @Override
    public void execute(ExecutableBranch branch) {
        throw new RuntimeException("Cannot execute instance");
    }

    public ClassPrototype getClassPrototype() {
        return prototype;
    }

    @Override
    public Value[] getVariables() {
        return fieldValues;
    }

    @Override
    public ClassScope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "@(instance of " + prototype.getGroup().getObject().getName() + ":" + prototype.getClassName() + ")";
    }

}
