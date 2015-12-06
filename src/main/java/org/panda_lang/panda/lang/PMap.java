package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.scheme.ConstructorScheme;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Parameter;

import java.util.HashMap;
import java.util.Map;

public class PMap extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PMap.class, "Map");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PMap>() {
            @Override
            public PMap run(Parameter... parameters) {
                return new PMap();
            }
        }));
        // Method: put
        os.registerMethod(new MethodScheme("put", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PMap map = instance.getValue(PMap.class);
                return map.getMap().put(parameters[0].getValue(), parameters[1].getValue());
            }
        }));
        // Method: get
        os.registerMethod(new MethodScheme("get", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PMap map = instance.getValue(PMap.class);
                return map.getMap().get(parameters[0].getValue());
            }
        }));
    }

    private final Map<PObject, PObject> map;

    public PMap() {
        this.map = new HashMap<>();
    }

    public Map<PObject, PObject> getMap() {
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
