package org.panda_lang.panda.framework.language.architecture.dynamic;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public abstract class AbstractScopeInstance<T extends Scope> implements ScopeInstance {

    protected final T scope;
    protected final Value[] localMemory;

    protected AbstractScopeInstance(T scope, int localMemory) {
        this.scope = scope;
        this.localMemory = new Value[localMemory];
    }

    protected AbstractScopeInstance(T scope) {
        this(scope, scope.getVariables().size());
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= localMemory.length) {
            throw new PandaRuntimeException("Invalid variable index: " + index + "; Amount of localMemory: " + getAmountOfVariables());
        }
    }

    @Override
    public synchronized void set(int pointer, @Nullable Value value) {
        checkIndex(pointer);
        localMemory[pointer] = value;
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
