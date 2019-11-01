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

package org.panda_lang.panda.shell.cli;

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.shell.PandaShell;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

final class PandaCommandTest {

    private static final PandaShell SHELL = new PandaShell(LoggerFactory.getLogger(PandaCommandTest.class));

    @Test
    void help() throws Exception {
        invoke("--help");
    }

    @Test
    void version() throws Exception {
        invoke("--version");
    }

    private void invoke(String args) throws Exception {
        CommandLine.populateCommand(new PandaCommand(SHELL), args).run();
    }

}
