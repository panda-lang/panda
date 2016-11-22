package org.panda_lang.panda.implementation.structure;

import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.structure.WrapperLinker;

import java.util.Stack;

public class PandaWrapperLinker implements WrapperLinker {

    private final Stack<Wrapper> wrapperStack;

    public PandaWrapperLinker() {
        this.wrapperStack = new Stack<>();
    }

    @Override
    public int reserveSlot() {
        return 0;
    }

    @Override
    public void linkWrapper(int id, Wrapper wrapper) {

    }

    @Override
    public Wrapper getCurrentWrapper() {
        return wrapperStack.peek();
    }

}
