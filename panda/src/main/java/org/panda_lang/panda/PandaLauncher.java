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

package org.panda_lang.panda;

import org.panda_lang.language.interpreter.logging.Logger;
import org.panda_lang.language.interpreter.logging.SystemLogger;
import org.panda_lang.panda.shell.PandaShell;
import org.panda_lang.utilities.commons.ArrayUtils;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Default launcher of Panda
 */
public final class PandaLauncher {

    private PandaLauncher() { }

    /**
     * Launch Panda with the given parameters
     *
     * @param args arguments passed to the shell, filled with '--help' if empty
     * @throws Exception if something happen
     */
    public static void main(String... args) throws Exception {
        launch(SystemLogger::new, System.in, args);
    }

    public static void launch(Supplier<Logger> logger, InputStream input, String... args) throws Exception {
        PandaShell shell = new PandaShell(logger, input);

        if (ArrayUtils.isEmpty(args)) {
            args = new String[] { "--help" };
        }

        shell.invoke(args);
    }

}
