package org.panda_lang.core.runtime.structure;

import org.panda_lang.core.runtime.element.WrapperInstance;

public interface ExecutableBranch {

    ExecutableBranch getChild();

    ExecutableBranch getParent();

    WrapperInstance getWrapperInstance();

}
