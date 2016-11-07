package org.panda_lang.panda.util.embed;

import java.util.ArrayList;
import java.util.List;

public class PandaEngineFactoryConstants {

    /**
     * Language name
     */
    public static final String NAME = "Panda";

    /**
     * Panda version
     */
    public static final String VERSION = "indev-0.0.3-SNAPSHOT";

    /**
     * Engine name
     */
    public static final String ENGINE_NAME = "Panda Core";

    /**
     * Engine version
     */
    public static final String ENGINE_VERSION = VERSION;

    /**
     * Extensions
     */
    public static final List<String> EXTENSIONS = new ArrayList<>();

    /**
     * Mime types
     */
    public static final List<String> MIME_TYPES = new ArrayList<>();


    /**
     * Names for Panda Engine
     */
    public static final List<String> NAMES = new ArrayList<>();


    static {
        EXTENSIONS.add(".panda");

        MIME_TYPES.add("application/panda");
        MIME_TYPES.add("text/panda");

        NAMES.add(NAME);
        NAMES.add(ENGINE_NAME);
    }

}
