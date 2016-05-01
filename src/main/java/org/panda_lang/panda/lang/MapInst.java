package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

import java.util.HashMap;
import java.util.Map;

public class MapInst extends ObjectInst {

    static {
        Structure structure = new Structure("Map");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new MapInst();
            }
        });
        structure.method(new Method("put", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                MapInst map = alice.getValueOfInstance();
                return map.getMap().put(alice.getValueOfFactor(0), alice.getValueOfFactor(1));
            }
        }));
        structure.method(new Method("get", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                MapInst map = alice.getValueOfInstance();
                return map.getMap().get(alice.getValueOfFactor(0));
            }
        }));
    }

    private final Map<Inst, Inst> map;

    public MapInst() {
        this.map = new HashMap<>();
    }

    public Map<Inst, Inst> getMap() {
        return map;
    }

    @Override
    public Object getJavaValue() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
