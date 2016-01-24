package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PFloat extends PObject {

    static {
        Vial vial = new Vial("Float");
        vial.group("panda.lang");
    }

    private final float f;

    public PFloat(float f) {
        this.f = f;
    }

    public float getFloat() {
        return f;
    }

    @Override
    public Object getJavaValue() {
        return getFloat();
    }

}
