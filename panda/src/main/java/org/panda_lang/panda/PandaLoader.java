package org.panda_lang.panda;

import org.panda_lang.core.Application;
import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.SourceSet;
import org.panda_lang.panda.lang.PandaApplication;
import org.panda_lang.panda.lang.interpreter.PandaInterpreter;
import org.panda_lang.panda.lang.interpreter.PandaSourceSet;

import java.io.File;

public class PandaLoader {

    private final Panda panda;

    public PandaLoader(Panda panda) {
        this.panda = panda;
    }

    public PandaApplication loadSingleFile(File file) {
        if (file == null) {
            System.out.println("[PandaLoader] File is null");
            return null;
        }
        else if (!file.exists()) {
            System.out.println("[PandaLoader] File '" + file.getName() + "' doesn't exist.");
            return null;
        }

        PandaSourceSet pandaSourceSet = new PandaSourceSet();
        pandaSourceSet.add(file);

        PandaInterpreter interpreter = new PandaInterpreter(panda.getPandaComposition().getPandaInterpreterConfiguration(), pandaSourceSet);
        PandaApplication application = interpreter.interpret();

        application.setWorkingDirectory(file.getParent());
        return application;
    }

}
