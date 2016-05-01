package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Structure;

public class CharInst extends ObjectInst {

    static {
        Structure structure = new Structure("Char");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                if (alice.hasFactors()) {
                    return alice.getValueOfFactor(0);
                }
                return new CharInst('\u0000');
            }
        });
    }

    private final char c;

    public CharInst(char c) {
        this.c = c;
    }

    @Override
    public String getType() {
        return "Character";
    }

    @Override
    public Object getJavaValue() {
        return c;
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

}
