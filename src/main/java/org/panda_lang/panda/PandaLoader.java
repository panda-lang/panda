package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.PandaParser;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;

public class PandaLoader {

    public static PandaScript loadSimpleScript(File file) {
        if (file.isDirectory()) {
            return null;
        }
        return loadSimpleScript(IOUtils.getContentOfFile(file));
    }

    public static PandaScript loadSimpleScript(String source) {
        final PandaParser parser = new PandaParser(source);
        return parser.parse();
    }

}
