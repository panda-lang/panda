package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

import java.util.ArrayList;
import java.util.List;

public class ListInst extends ObjectInst {

    static {
        Structure structure = new Structure("List");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new ListInst();
            }
        });
        structure.method(new Method("add", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ListInst list = alice.getValueOfInstance();
                list.getList().add(alice.getValueOfFactor(0));
                return null;
            }
        }));
    }

    private final List<Inst> list;

    public ListInst() {
        this.list = new ArrayList<>();
    }

    public List<Inst> getList() {
        return list;
    }

    @Override
    public Object getJavaValue() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (Inst o : list) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(o);
        }
        return node.toString();
    }

}
