package org.panda_lang.panda.implementation.interpreter.parser.linker;

import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.interpreter.parser.linker.WrapperLinker;

import java.util.Stack;

public class PandaWrapperLinker implements WrapperLinker {

    private final Stack<Wrapper> wrapperStack;

    public PandaWrapperLinker() {
        this.wrapperStack = new Stack<>();
    }

    @Override
    public void pushWrapper(Wrapper wrapper) {
        wrapperStack.push(wrapper);
    }

    @Override
    public Wrapper popWrapper() {
        return wrapperStack.pop();
    }

    @Override
    public Wrapper getCurrentWrapper() {
        return wrapperStack.peek();
    }

    @Override
    public int getNextID() {
        return wrapperStack.size();
    }

}
