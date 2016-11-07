package org.panda_lang.panda;

import org.panda_lang.panda.implementation.runtime.PandaApplication;

import java.io.File;

public class DefaultTest {

    private static final File SCRIPT = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        PandaLoader pandaLoader = panda.getPandaLoader();
        PandaApplication pandaApplication = pandaLoader.loadSingleFile(SCRIPT);

        pandaApplication.launch(args);
    }

}
