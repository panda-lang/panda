package org.panda_lang.panda.lang.element;

import org.panda_lang.core.work.HeadWrapper;

public class Method extends Block implements HeadWrapper {

    @Override
    public String getType() {
        return "method";
    }

}
