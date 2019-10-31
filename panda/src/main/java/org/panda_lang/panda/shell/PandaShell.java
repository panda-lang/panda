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

package org.panda_lang.panda.shell;

import org.panda_lang.panda.shell.cli.PandaCommand;
import org.slf4j.Logger;
import picocli.CommandLine;

public final class PandaShell {

    private Logger logger;

    public PandaShell(Logger logger) {
        this.logger = logger;
    }

    public void invoke(String... args) {
        PandaCommand command = CommandLine.populateCommand(new PandaCommand(this), args);
        command.run();
    }

    public Logger getLogger() {
        return logger;
    }

}