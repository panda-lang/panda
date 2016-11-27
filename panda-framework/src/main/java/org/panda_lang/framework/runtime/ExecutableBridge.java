package org.panda_lang.framework.runtime;

import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Value;

public interface ExecutableBridge {

    void call(Executable executable);

    void returnValue(Value value);

    Value[] getParameters();

}
