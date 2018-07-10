package org.panda_lang.panda.framework.language.architecture.dynamic;

import org.panda_lang.panda.framework.design.architecture.value.Value;

public class AbstractScopeInstanceUtils {

    public static Value[] extractMemory(AbstractScopeInstance<?> scopeInstance) {
        return scopeInstance.localMemory;
    }

}
