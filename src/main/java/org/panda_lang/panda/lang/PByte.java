package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PByte extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("Byte");
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
