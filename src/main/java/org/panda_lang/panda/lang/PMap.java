package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.syntax.Essence;

import java.util.HashMap;
import java.util.Map;

public class PMap extends PObject {

    /*
    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PMap.class, "Map");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PMap>() {
            @Override
            public PMap run(Factor... factors) {
                return new PMap();
            }
        }));
        // Method: put
        os.registerMethod(new MethodScheme("put", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PMap map = instance.getValue(PMap.class);
                return map.getMap().put(factors[0].getValue(), factors[1].getValue());
            }
        }));
        // Method: get
        os.registerMethod(new MethodScheme("get", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PMap map = instance.getValue(PMap.class);
                return map.getMap().get(factors[0].getValue());
            }
        }));
    }
    */

    private final Map<Essence, Essence> map;

    public PMap() {
        this.map = new HashMap<>();
    }

    public Map<Essence, Essence> getMap() {
        return map;
    }

    @Override
    public String getType() {
        return "Map";
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
