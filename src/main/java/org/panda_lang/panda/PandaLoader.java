package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.work.Wrapper;
import org.panda_lang.panda.lang.parser.PandaParser;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;

public class PandaLoader {

    // FIXME: 6/20/2016
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
