package org.panda_lang.core.structure;

import org.jetbrains.annotations.Nullable;

public interface Executable extends Statement {

    /**
     * Call executable
     *
     * @param parametersValues parameters for executable process
     * @return result of executed code
     */

    @Nullable
    Value execute(Value... parametersValues);

}
