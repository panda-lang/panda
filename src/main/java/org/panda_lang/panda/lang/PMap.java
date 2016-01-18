package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.*;

import java.util.HashMap;
import java.util.Map;

public class PMap extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("Map");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PMap();
            }
        });
        vial.method(new Method("put", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PMap map = particle.getValueOfInstance();
                return map.getMap().put(particle.getValueOfFactor(0), particle.getValueOfFactor(1));
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PMap map = particle.getValueOfInstance();
                return map.getMap().get(particle.getValueOfFactor(0));
            }
        }));
    }

    private final Map<Essence, Essence> map;

    public PMap() {
        super(vial);
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
