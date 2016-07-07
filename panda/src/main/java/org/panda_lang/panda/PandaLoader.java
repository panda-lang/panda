package org.panda_lang.panda;

import org.panda_lang.panda.lang.PandaApplication;

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

        return new PandaApplication();
    }

}
