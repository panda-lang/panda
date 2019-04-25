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

package org.panda_lang.panda.framework.language.architecture.dynamic;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public abstract class AbstractScopeFrame<T extends Scope> implements ScopeFrame {

    protected final T scope;
    protected final Value[] localMemory;

    protected AbstractScopeFrame(T scope, int localMemory) {
        this.scope = scope;
        this.localMemory = new Value[localMemory];
    }

    protected AbstractScopeFrame(T scope) {
        this(scope, scope.getVariables().size());
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= localMemory.length) {
            throw new PandaRuntimeException("Invalid variable index: " + index + "; Amount of localMemory: " + getAmountOfVariables());
        }
    }

    @Override
    public synchronized Value set(int pointer, @Nullable Value value) {
        checkIndex(pointer);
        localMemory[pointer] = value;
        return value;
    }

    @Override
    public synchronized @Nullable Value get(int pointer) {
        checkIndex(pointer);
        return localMemory[pointer];
    }

    @Override
    public int getAmountOfVariables() {
        return localMemory.length;
    }

    @Override
    public T getScope() {
        return scope;
    }

}
