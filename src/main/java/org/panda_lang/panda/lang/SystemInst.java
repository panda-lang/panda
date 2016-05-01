package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

public class SystemInst extends ObjectInst {

    static {
        Structure structure = new Structure("System");
        structure.group("panda.lang");
        structure.method(new Method("print", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                System.out.println(alice.getValueOfFactor(0));
                return null;
            }
        }));
        structure.method(new Method("currentTimeMillis", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new LongInst(System.currentTimeMillis());
            }
        }));
        structure.method(new Method("nanoTime", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new LongInst(System.nanoTime());
            }
        }));
        structure.method(new Method("exit", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                System.exit(-1);
                return new IntInst(-1);
            }
        }));
    }

}

