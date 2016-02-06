package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.block.MethodBlock;

import java.io.File;

public class ExampleTest
{

    private static final File SCRIPT = new File("number_types.sp");
    private static final File DIRECTORY = new File("examples");

    public static void main(String[] args)
    {
        Panda.getInstance();
        PandaScript pandaScript = PandaLoader.loadSingleScript(new File(DIRECTORY + File.separator + SCRIPT));
        pandaScript.setWorkingDirectory("/");
        pandaScript.call(MethodBlock.class, "main");
    }

}
