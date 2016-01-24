package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PShort extends PObject {

    static {
        Vial vial = new Vial("Short");
        vial.group("panda.lang");
    }

    private final short s;

    public PShort(short s) {
        this.s = s;
    }

    public short getShort() {
        return s;
    }

    @Override
    public Object getJavaValue() {
        return getShort();
    }

}
