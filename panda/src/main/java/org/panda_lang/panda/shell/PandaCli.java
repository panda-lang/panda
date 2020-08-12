/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.shell;

import org.panda_lang.language.interpreter.messenger.PandaMessenger;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaConstants;
import org.panda_lang.panda.PandaFactory;
import org.panda_lang.panda.manager.PackageManager;
import org.panda_lang.panda.shell.repl.ReplConsole;
import org.panda_lang.panda.util.PandaUtils;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;
import org.tinylog.configuration.Configuration;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "panda", version = "Panda " + PandaConstants.VERSION)
final class PandaCli implements ThrowingRunnable<Exception> {

    private final PandaShell shell;

    @Parameters(index = "0", paramLabel = "<script>", description = "script to load", defaultValue = "")
    private File script;

    @Option(names = { "--version", "-V" }, versionHelp = true, description = "display current version of panda")
    private boolean versionInfoRequested;

    @Option(names = { "--help", "-H" }, usageHelp = true, description = "display help message")
    private boolean usageHelpRequested;

    @Option(names = { "--level", "-L" }, description = "set level of logging", paramLabel = "<level>")
    private String level;

    @Option(names = { "--repl", "-R" }, description = "open interactive shell")
    private boolean repl;

    @Option(names = { "--simplified-repl", "-S" }, description = "open interactive shell with simplified errors")
    private boolean simplifiedRepl;

    public PandaCli(PandaShell shell) {
        this.shell = shell;
    }

    @Override
    public void run() throws Exception {
        CommandLine commandLine = new CommandLine(this);

        if (level != null) {
            Configuration.set("level", level);
        }

        if (usageHelpRequested) {
            CommandLine.usage(this, System.out);
            return;
        }

        if (versionInfoRequested) {
            commandLine.printVersionHelp(System.out);
            return;
        }

        if (!repl && !simplifiedRepl && script == null) {
            shell.getLogger().warn("Missing or unknown operation");
            return;
        }

        PandaUtils.printJVMUptime(shell);
        Panda panda = new PandaFactory().createPanda(shell.getLogger());

        if (repl || simplifiedRepl) {
            ReplConsole console = new ReplConsole(panda, shell.getInput(), simplifiedRepl);
            console.launch();
            return;
        }

        if (script == null) {
            shell.getLogger().error("You have to specify the script file. Use --help to find out more about the shell in general.");
            return;
        }

        if (script.getName().endsWith("panda.hjson")) {
            PackageManager packageManager = new PackageManager(new PandaMessenger(panda), script.getParentFile());
            packageManager.install(script);
            packageManager.run(panda, script);
            return;
        }

        panda.getLoader()
                .load(script, script.getParentFile())
                .launch();
    }

}