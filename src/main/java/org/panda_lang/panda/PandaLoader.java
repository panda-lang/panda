package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.PandaParser;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PandaLoader {

    public static Collection<PandaScript> loadDirectory(File directory) {
        Collection<PandaScript> scripts = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        Collection<PandaScript> otherScripts = loadDirectory(file);
                        scripts.addAll(otherScripts);
                    } else {
                        PandaScript pandaScript = loadSingleScript(file);
                        scripts.add(pandaScript);
                    }
                }
            }
        } else {
            PandaScript pandaScript = loadSingleScript(directory);
            scripts.add(pandaScript);
        }
        return scripts;
    }

    public static PandaScript loadSingleScript(File file) {
        if (file.isDirectory()) {
            return null;
        }

        final String contentOfFile = IOUtils.getContentOfFile(file);

        if (contentOfFile == null) {
            System.out.println("File '" + file.getName() + "' doesn't exist.");
            return null;
        }

        return loadSingleScript(contentOfFile, file.getParent());
    }

    public static PandaScript loadSingleScript(String source, String workingDirectory) {
        final PandaParser parser = new PandaParser(source);
        parser.getPandaScript().setWorkingDirectory(workingDirectory);
        return parser.parse();
    }

    public static PandaScript loadSimpleScript(String source) {
        final PandaParser parser = new PandaParser(source);
        return parser.parse();
    }

}
