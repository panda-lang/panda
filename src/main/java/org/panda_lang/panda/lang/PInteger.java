package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PInteger extends PObject {

    static {
        Vial vial = new Vial("Integer");
        vial.group("panda.lang");
    }

    private final int i;

    public PInteger(int i) {
        this.i = i;
    }

    public int getInteger() {
        return i;
    }

    @Override
    public Object getJavaValue() {
        return getInteger();
    }
    
}
