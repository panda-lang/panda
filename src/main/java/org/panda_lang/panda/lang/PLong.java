package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Vial;

public class PLong extends PObject {

    static {
        Vial vial = new Vial("Long");
        vial.group("panda.lang");
    }

    private final long l;

    public PLong(long l) {
        this.l = l;
    }

    public long getLong() {
        return l;
    }

    @Override
    public Object getJavaValue() {
        return getLong();
    }

}
