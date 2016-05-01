package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

public class NullInst extends Inst {

    static {
        Structure structure = new Structure("null");
        structure.group("panda.lang");
        structure.method(new Method("toString", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new StringInst("null");
            }
        }));
    }

    public NullInst() {
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
