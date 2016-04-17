package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Vial;

public class StringEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("String");
        vial.group("panda.lang");
        vial.constructor(new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return new StringEssence(alice.getValueOfFactor(0).toString());
            }
        });
    }

    private final String string;

    public StringEssence(String string) {
        this.string = string;
    }

    public BooleanEssence contains(ObjectEssence o) {
        return new BooleanEssence(string.contains(o.toString()));
    }

    public StringEssence replace(ObjectEssence f, ObjectEssence t) {
        String from = f.toString();
        String to = t.toString();
        return new StringEssence(string.replace(from, to));
    }

    @Override
    public Object getJavaValue() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

}
