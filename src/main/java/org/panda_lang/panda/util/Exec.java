package org.panda_lang.panda.util;

import org.panda_lang.panda.PandaLoader;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.syntax.block.MethodBlock;

import java.io.File;

public class Exec {

    public static void activate(String source) {
        try {
            if (source == null || source.isEmpty()) return;
            String[] parts = source.split("=", 2);

            if (parts.length < 1 || parts[0] == null) return;
            String command = parts[0].toUpperCase();

            switch (command) {
                case "-FILE":
                    PandaScript fScript = PandaLoader.loadSingleScript(new File(parts[1]));
                    if (fScript == null) return;
                    fScript.call(MethodBlock.class, "main");
                    break;
                case "-SOURCE":
                    PandaScript sScript = PandaLoader.loadSimpleScript(parts[1]);
                    if (sScript == null) return;
                    sScript.call(MethodBlock.class, "main");
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
