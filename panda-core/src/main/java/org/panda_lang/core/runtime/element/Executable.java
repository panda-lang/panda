package org.panda_lang.core.runtime.element;

import com.sun.istack.internal.Nullable;

public interface Executable {

    /**
     * Call executable
     *
     * @param parameters parameters for executable process
     * @return result of executed code
     */
    @Nullable
    Value execute(Value... parameters);

}
