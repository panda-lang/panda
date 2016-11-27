package org.panda_lang.framework.structure;

import org.panda_lang.framework.runtime.ExecutableBridge;

public interface Executable extends Statement {

    void execute(ExecutableBridge executionInfo);

}
