package org.panda_lang.panda;

import org.panda_lang.panda.core.statement.block.MethodBlock;

import java.io.File;

public class ExampleTest {

    private static final File SCRIPT = new File("return.panda");
    private static final File DIRECTORY = new File("examples");

    public static void main(String[] args) {
        Panda panda = new Panda();
        panda.initializeDefaultElements();

        PandaScript pandaScript = panda.getPandaLoader().loadSingleScript(new File(DIRECTORY + File.separator + SCRIPT));
        pandaScript.setWorkingDirectory("/");
        pandaScript.call(MethodBlock.class, "main");
    }

}
