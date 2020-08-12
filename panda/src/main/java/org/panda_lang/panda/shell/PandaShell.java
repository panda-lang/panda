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

import org.panda_lang.language.interpreter.messenger.LoggerHolder;
import org.panda_lang.utilities.commons.function.Lazy;
import org.slf4j.Logger;
import picocli.CommandLine;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Represents command line user interface of Panda
 */
public final class PandaShell implements LoggerHolder {

    private final Supplier<Logger> logger;
    private final InputStream input;
    private final PandaCli pandaCli = new PandaCli(this);

    /**
     * Create a new instance of shell
     *
     * @param logger the logger used to send responses
     */
    public PandaShell(Supplier<Logger> logger, InputStream input) {
        this.logger = new Lazy<>(logger);
        this.input = input;
    }

    /**
     * Invoke the given arguments
     *
     * @param args array of arguments
     * @throws Exception if something happen
     */
    public void invoke(String... args) throws Exception {
        PandaCli command = CommandLine.populateCommand(pandaCli, args);
        command.run();
    }

    /**
     * Get input of shell
     *
     * @return the input
     */
    public InputStream getInput() {
        return input;
    }

    @Override
    public Logger getLogger() {
        return logger.get();
    }

}