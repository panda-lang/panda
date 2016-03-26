package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class CharEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Char");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                if (alice.hasFactors()) {
                    return alice.getValueOfFactor(0);
                }
                return new CharEssence('\u0000');
            }
        });
    }

    private final char c;

    public CharEssence(char c) {
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
