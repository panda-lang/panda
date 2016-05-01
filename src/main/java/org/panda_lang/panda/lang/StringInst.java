package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Structure;

public class StringInst extends ObjectInst {

    static {
        Structure structure = new Structure("String");
        structure.group("panda.lang");
        structure.constructor(new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new StringInst(alice.getValueOfFactor(0).toString());
            }
        });
    }

    private final String string;

    public StringInst(String string) {
        this.string = string;
    }

    public BooleanInst contains(ObjectInst o) {
        return new BooleanInst(string.contains(o.toString()));
    }

    public StringInst replace(ObjectInst f, ObjectInst t) {
        String from = f.toString();
        String to = t.toString();
        return new StringInst(string.replace(from, to));
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
