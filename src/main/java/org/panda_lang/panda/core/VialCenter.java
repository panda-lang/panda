package org.panda_lang.panda.core;

import org.panda_lang.panda.core.syntax.Vial;

import java.util.HashMap;
import java.util.Map;

public class VialCenter {

    private static final Map<String, Vial> vials = new HashMap<>();

    public static Vial initializeVial(String vialName) {
        Vial vial = new Vial(vialName);
        return vials.put(vialName, vial);
    }

    public static void registerVial(Vial vial) {
        vials.put(vial.getName(), vial);
    }

    public static Vial getVial(String vialName) {
        return vials.get(vialName);
    }

}
