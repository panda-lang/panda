/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.shell.repl;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaConstants;

import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

public final class ReplConsole {

    private final FrameworkController controller;
    private final InputStream input;
    private final boolean simplified;
    private boolean interrupted;

    public ReplConsole(Panda controller, InputStream input, boolean simplified) {
        this.controller = controller;
        this.input = input;
        this.simplified = simplified;
    }

    public void launch() throws Exception {
        Scanner scanner = new Scanner(input);
        controller.getLogger().info("Panda " + PandaConstants.VERSION + " REPL");
        controller.getLogger().info("Type 'help' for more information.");

        Repl repl = Repl.creator(this)
                .withCustomExceptionListener(simplified ? (exception, runtime) -> controller.getLogger().error(exception.getMessage()) : null)
                .create();

        while (!interrupted && scanner.hasNextLine()) {
            Collection<ReplResult> results = repl.evaluate(scanner.nextLine());
            ReplUtils.print(controller, results);
        }

        controller.getLogger().info("REPL has been terminated");
    }

    public void interrupt() {
        this.interrupted = true;
    }

    public FrameworkController getFrameworkController() {
        return controller;
    }

}
