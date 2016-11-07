package org.panda_lang.panda.launcher;

import org.panda_lang.core.runtime.Application;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;
import org.panda_lang.panda.PandaLoader;

import java.io.File;
import java.util.Collection;

public class PandaLauncher {

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaLoader pandaLoader = panda.getPandaLoader();
        Collection<File> files = FileUtils.findFilesByExtension(new File(System.getProperty("user.dir")), ".panda");

        for (File file : files) {
            Application application = pandaLoader.loadSingleFile(file);
            application.launch(args);
        }
    }

}
