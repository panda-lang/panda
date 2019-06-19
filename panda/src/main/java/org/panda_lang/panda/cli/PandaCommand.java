/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.cli;

import org.panda_lang.panda.PandaConstants;
import org.panda_lang.panda.framework.design.architecture.Application;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "panda", version = "Panda " + PandaConstants.VERSION)
public class PandaCommand implements Runnable {

    private final PandaCLI cli;

    @CommandLine.Parameters(index = "0", paramLabel = "SCRIPT", description = "Script to load")
    private File script;

    @CommandLine.Option(names = { "-V", "--version" }, versionHelp = true, description = "display Panda version")
    private boolean versionInfoRequested;

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "display help message")
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

        Application application = cli.getPanda().getPandaLoader().load(script, script.getParentFile());
        application.launch();
    }

}