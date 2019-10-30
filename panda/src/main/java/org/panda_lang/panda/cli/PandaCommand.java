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

import org.panda_lang.framework.PandaFramework;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.panda.PandaConstants;
import org.panda_lang.panda.PandaFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.Optional;

@Command(name = "panda", version = "Panda " + PandaConstants.VERSION)
public final class PandaCommand implements Runnable {

    private final PandaCLI cli;

    @Parameters(index = "0", paramLabel = "SCRIPT", description = "Script to load")
    private File script;

    @Option(names = { "-V", "--version" }, versionHelp = true, description = "display Panda version")
    private boolean versionInfoRequested;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "display help message")
    private boolean usageHelpRequested;

    @Option(names = { "-l", "--level" }, description = "set level of logging", paramLabel="<level>")
    private String level;

    public PandaCommand(PandaCLI cli) {
        this.cli = cli;
    }

    @Override
    public void run() {
        CommandLine commandLine = new CommandLine(this);

        if (level != null) {
            System.setProperty("tinylog.writer.level", level);
            // todo: replace after tinylog fix
            // Configuration.set("tinylog.writer.level", level);
        }

        if (usageHelpRequested) {
            CommandLine.usage(this, System.out);
            return;
        }

        if (versionInfoRequested) {
            commandLine.printVersionHelp(System.out);
            return;
        }

        Optional<Application> application = new PandaFactory()
                .createPanda().getLoader()
                .load(script, script.getParentFile());

        if (!application.isPresent()) {
            PandaFramework.getLogger().error("Cannot load application");
            return;
        }

        application.get().launch();
    }

}