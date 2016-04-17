package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

public class NullEssence extends Essence {

    static {
        Vial vial = new Vial("null");
        vial.group("panda.lang");
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return new StringEssence("null");
            }
        }));
    }

    public NullEssence() {
    }

    @Override
    public Object getJavaValue() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

}
