package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

public class SystemEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("System");
        vial.group("panda.lang");
        vial.method(new Method("print", new Executable() {
            @Override
            public Essence run(Alice alice) {
                System.out.println(alice.getValueOfFactor(0));
                return null;
            }
        }));
        vial.method(new Method("currentTimeMillis", new Executable() {
            @Override
            public Essence run(Alice alice) {
                return new LongEssence(System.currentTimeMillis());
            }
        }));
        vial.method(new Method("nanoTime", new Executable() {
            @Override
            public Essence run(Alice alice) {
                return new LongEssence(System.nanoTime());
            }
        }));
        vial.method(new Method("exit", new Executable() {
            @Override
            public Essence run(Alice alice) {
                System.exit(-1);
                return new IntEssence(-1);
            }
        }));
    }

}

