package org.panda_lang.core.work.structure;

import org.panda_lang.core.work.element.WrapperInstance;

public interface ExecutableBranch {

    ExecutableBranch getChild();

    ExecutableBranch getParent();

    WrapperInstance getWrapperInstance();

}
