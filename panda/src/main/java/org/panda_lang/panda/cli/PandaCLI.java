package org.panda_lang.panda.cli;

import org.panda_lang.panda.Panda;
import picocli.CommandLine;

public class PandaCLI {

    private final Panda panda;

    public PandaCLI(Panda panda) {
        this.panda = panda;
    }

    public void run(String... args) {
        CommandLine.run(new PandaCommand(this), args);
    }

    public Panda getPanda() {
        return panda;
    }

}
