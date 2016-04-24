package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.*;

import java.util.Arrays;

public class ArrayEssence extends Essence {

    static {
        Vial vial = new Vial("Array");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence execute(Alice alice) {
                return new ArrayEssence(alice.getRuntimeValues());
            }
        });
        vial.method(new Method("size", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                ArrayEssence array = alice.getValueOfInstance();
                return new IntEssence(array.getArray().length);
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                Numeric numeric = alice.getValueOfFactor(0);
                ArrayEssence instance = alice.getValueOfInstance();
                return instance.get(alice, numeric.getInt());
            }
        }));
    }

    private final RuntimeValue[] array;

    public ArrayEssence(RuntimeValue... values) {
        this.array = values;
    }

    public Essence get(Alice alice, int i) {
        return i < array.length ? array[i].getValue(alice) : new NullEssence();
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
