package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PDouble extends PObject {

    static {
        Vial vial = new Vial("Double");
        vial.group("panda.lang");
    }

    private final double d;

    public PDouble(double d) {
        this.d = d;
    }

    public double getDouble() {
        return d;
    }

    @Override
    public Object getJavaValue() {
        return getDouble();
    }

}
