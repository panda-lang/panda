package org.panda_lang.panda.implementation.work.structure;

import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.element.Executable;
import org.panda_lang.core.work.element.Scope;
import org.panda_lang.core.work.element.Wrapper;
import org.panda_lang.core.work.structure.ExecutableTree;

public class PandaExecutableTree implements ExecutableTree {

    @Override
    public Value call(Executable executable, Value... parameters) {
        if (executable instanceof Scope) {
            return callScope((Scope) executable, parameters);
        }

        return executable.execute(parameters);
    }

    public Value callScope(Scope scope, Value... parameters) {
        if (scope instanceof Wrapper) {
            return callWrapper((Wrapper) scope, parameters);
        }

        return null;
    }

    public Value callWrapper(Wrapper wrapper, Value... parameters) {
        return null;
    }

}
