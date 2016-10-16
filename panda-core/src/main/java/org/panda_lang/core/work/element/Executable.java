package org.panda_lang.core.work.element;

import com.sun.istack.internal.Nullable;
import org.panda_lang.core.work.Value;

public interface Executable {

    /**
     * Call executable
     *
     * @param parameters parameters for executable process
     * @return result of executed code
     */
    @Nullable Value execute(Value... parameters);

}
