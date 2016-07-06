package org.panda_lang.panda;

import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.util.IOUtils;
import org.panda_lang.core.work.executable.Wrapper;
import org.panda_lang.panda.lang.interpreter.parser.PandaParser;

import java.io.File;

public class PandaLoader {

    public PandaApplication loadSingleFile(File file) {
        if (file == null) {
            System.out.println("[PandaLoader] File is null");
            return null;
        }
        else if (!file.exists()) {
            System.out.println("[PandaLoader] File '" + file.getName() + "' doesn't exist.");
            return null;
        }

        String contentOfFile = IOUtils.getContentOfFile(file);
        PandaParser pandaParser = new PandaParser(contentOfFile);
        Wrapper wrapper = pandaParser.parse(new ParserInfo());

        PandaApplication pandaApplication = new PandaApplication();
        PandaScript pandaScript = new PandaScript(wrapper);
        pandaScript.setWorkingDirectory(file.getParent());
        pandaApplication.addPandaScript(pandaScript);

        return pandaApplication;
    }

}
