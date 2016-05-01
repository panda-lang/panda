package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Structure;

public class BooleanInst extends ObjectInst {

    static {
        Structure structure = new Structure("Boolean");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                Inst inst = alice.getValueOfFactor(0);
                if (inst instanceof NullInst) {
                    return new BooleanInst(false);
                }
                else if (inst instanceof BooleanInst) {
                    return new BooleanInst(((BooleanInst) inst).getBoolean());
                }
                else if (inst instanceof Numeric) {
                    byte value = ((Numeric) inst).getByte();
                    return new BooleanInst(value != 0);
                }
                return new BooleanInst(false);
            }
        });
    }

    private final boolean b;

    public BooleanInst(boolean b) {
        this.b = b;
    }

    public boolean isTrue() {
        return b;
    }

    public boolean isFalse() {
        return !b;
    }

    public boolean getBoolean() {
        return b;
    }

    @Override
    public Object getJavaValue() {
        return b;
    }

    @Override
    public String toString() {
        return Boolean.toString(b);
    }

}
