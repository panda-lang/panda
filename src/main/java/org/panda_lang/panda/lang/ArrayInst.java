package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.*;

import java.util.Arrays;

public class ArrayInst extends Inst {

    static {
        Structure structure = new Structure("Array");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new ArrayInst(alice.getRuntimeValues());
            }
        });
        structure.method(new Method("size", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ArrayInst array = alice.getValueOfInstance();
                return new IntInst(array.getArray().length);
            }
        }));
        structure.method(new Method("get", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                Numeric numeric = alice.getValueOfFactor(0);
                ArrayInst instance = alice.getValueOfInstance();
                return instance.get(alice, numeric.getInt());
            }
        }));
    }

    private final RuntimeValue[] array;

    public ArrayInst(RuntimeValue... values) {
        this.array = values;
    }

    public Inst get(Alice alice, int i) {
        return i < array.length ? array[i].getValue(alice) : new NullInst();
    }

    public RuntimeValue[] getArray() {
        return array;
    }

    @Override
    public Object getJavaValue() {
        return array;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

}
