package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

public class NumberInst extends Inst {

    static {
        Structure structure = new Structure("Number");
        structure.group("panda.lang");
        structure.constructor(new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return null;
            }
        });
        structure.method(new Method("valueOf", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return null;
            }
        }));
        structure.method(new Method("toString", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                NumberInst number = alice.getInstance().getValue(alice);
                return new StringInst(number.toString());
            }
        }));
    }

    private Numeric number;

    protected NumberInst(Numeric number) {
        this.number = number;
    }

    public NumberType getNumberType() {
        return number.getNumberType();
    }

    @Override
    public Object getJavaValue() {
        return number;
    }

    @Override
    public String toString() {
        return number.toString();
    }

}
