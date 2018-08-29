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

package org.panda_lang.panda.framework.language.architecture.prototype;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractScopeInstance;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;

public class ClassScopeInstance extends AbstractScopeInstance<ClassScope> {

    private final ClassPrototype prototype;

    public ClassScopeInstance(ClassScope scope, ClassPrototype classPrototype) {
        super(scope, classPrototype.getFields().getAmountOfFields());
        this.prototype = classPrototype;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        throw new RuntimeException("Cannot execute instance");
    }

    public Value toValue() {
        return new PandaValue(prototype, this);
    }

    @Override
    public String toString() {
        return "@(instance of " + prototype.getClassName() + ")";
    }

}
