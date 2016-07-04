package org.panda_lang.panda;

import java.io.File;

public class DefaultPandaTest {

    private static final File SCRIPT = new File("examples/hello_panda.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaLoader pandaLoader = panda.getPandaLoader();
        PandaApplication pandaApplication = pandaLoader.loadSingleFile(SCRIPT);

        pandaApplication.launch(args);
    }

}
