package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.*;

import java.util.HashMap;
import java.util.Map;

public class MapEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Map");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                return new MapEssence();
            }
        });
        vial.method(new Method("put", new Executable() {
            @Override
            public Essence run(Alice alice) {
                MapEssence map = alice.getValueOfInstance();
                return map.getMap().put(alice.getValueOfFactor(0), alice.getValueOfFactor(1));
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence run(Alice alice) {
                MapEssence map = alice.getValueOfInstance();
                return map.getMap().get(alice.getValueOfFactor(0));
            }
        }));
    }

    private final Map<Essence, Essence> map;

    public MapEssence() {
        this.map = new HashMap<>();
    }

    public Map<Essence, Essence> getMap() {
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
