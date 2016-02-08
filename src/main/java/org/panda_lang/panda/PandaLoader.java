package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.PandaParser;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public Collection<PandaScript> loadDirectory(File directory) {
        Collection<PandaScript> scripts = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        Collection<PandaScript> otherScripts = loadDirectory(file);
                        scripts.addAll(otherScripts);
                    }
                    else {
                        PandaScript pandaScript = loadSingleScript(file);
                        scripts.add(pandaScript);
                    }
                }
            }
        }
        else {
            PandaScript pandaScript = loadSingleScript(directory);
            scripts.add(pandaScript);
        }
        return scripts;
    }

    public PandaScript loadSingleScript(File file) {
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

    public PandaScript loadSingleScript(String source, String workingDirectory) {
        final PandaParser parser = new PandaParser(panda, source);
        parser.getPandaScript().setWorkingDirectory(workingDirectory);
        return parser.parse();
    }

    public PandaScript loadSimpleScript(String source) {
        final PandaParser parser = new PandaParser(panda, source);
        return parser.parse();
    }

}
