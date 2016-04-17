package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

public class ObjectEssence extends Essence {

    static {
        Vial vial = new Vial("Object");
        vial.group("panda.lang");
        vial.extension(null);
        vial.constructor(new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return alice.hasFactors() ? new ObjectEssence(alice.getFactor(0)) : new ObjectEssence();
            }
        });
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return new StringEssence(alice.getInstance().getValue(alice).toString());
            }
        }));
    }

    private Object object;

    public ObjectEssence() {
    }

    public ObjectEssence(Object object) {
        this.object = object;
    }

    @Override
    public Object getJavaValue() {
        return object;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
