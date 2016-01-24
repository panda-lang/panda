package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PByte extends PObject {

    static {
        Vial vial = new Vial("Byte");
        vial.group("panda.lang");
    }

    private final byte b;

    public PByte(byte b) {
        this.b = b;
    }

    public byte getByte() {
        return b;
    }

    @Override
    public Object getJavaValue() {
        return getByte();
    }

}
