package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

public class ObjectInst extends Inst {

    static {
        Structure structure = new Structure("Object");
        structure.group("panda.lang");
        structure.extension(null);
        structure.constructor(new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return alice.hasFactors() ? new ObjectInst(alice.getFactor(0)) : new ObjectInst();
            }
        });
        structure.method(new Method("toString", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new StringInst(alice.getInstance().getValue(alice).toString());
            }
        }));
    }

    private Object object;

    public ObjectInst() {
    }

    public ObjectInst(Object object) {
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
