package org.panda_lang.panda.cli;

import org.panda_lang.panda.PandaConstants;
import org.panda_lang.panda.framework.design.architecture.Application;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "panda", version = "Panda " + PandaConstants.VERSION)
public class PandaCommand implements Runnable {

    private final PandaCLI cli;

    @CommandLine.Parameters(arity = "1..*", paramLabel = "FILE", description = "File(s) to load")
    private File[] inputFiles;

    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true, description = "display Panda version")
    private boolean versionInfoRequested;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display help message")
    private boolean usageHelpRequested;

    public PandaCommand(PandaCLI cli) {
        this.cli = cli;
    }

    @Override
    public void run() {
        CommandLine commandLine = new CommandLine(this);

        if (usageHelpRequested) {
            CommandLine.usage(this, System.out);
            return;
        }

        if (versionInfoRequested) {
            commandLine.printVersionHelp(System.out);
            return;
        }

        Application application = cli.getPanda().getPandaLoader().loadFiles(inputFiles);
        application.launch();
    }

}