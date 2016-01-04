package org.panda_lang.panda.core;

import org.panda_lang.panda.core.syntax.Vial;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;

public class VialCenter {

    private static final Map<String, Vial> vials = new HashMap<>();

    public static Vial initializeVial(String vialName) {
        Vial vial = new Vial(vialName);
        vials.put(vialName, vial);
        return vial;
    }

    public static void registerVial(Vial vial) {
        vials.put(vial.getName(), vial);
    }

    public static Vial getVial(String vialName) {
        return vials.get(vialName);
    }

    public static Collection<Vial> getVials() {
        return vials.values();
    }

}
