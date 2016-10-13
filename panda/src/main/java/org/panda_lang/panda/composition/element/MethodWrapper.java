package org.panda_lang.panda.composition.element;

import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.element.*;

import java.util.List;

public class MethodWrapper implements Wrapper {

    private final String methodName;

    public MethodWrapper(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public WrapperInstance createInstance() {
        return new MethodWrapperInstance(this);
    }

    @Override
    public ExecutableCell addExecutable(Executable executable) {
        return null;
    }

    @Override
    public List<ExecutableCell> getExecutableCells() {
        return null;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getName() {
        return getMethodName();
    }

    public static class MethodWrapperInstance implements WrapperInstance {

        private final MethodWrapper methodWrapper;

        public MethodWrapperInstance(MethodWrapper methodWrapper) {
            this.methodWrapper = methodWrapper;
        }

        @Override
        public Value execute() {
            return null;
        }

        @Override
        public MethodWrapper getWrapper() {
            return methodWrapper;
        }

    }

}
