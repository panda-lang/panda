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

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaConstants;

import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

public final class ReplConsole {

    private final Panda panda;
    private final InputStream input;
    private final boolean simplified;

    public ReplConsole(Panda panda, InputStream input, boolean simplified) {
        this.panda = panda;
        this.input = input;
        this.simplified = simplified;
    }

    public void launch() throws Exception {
        Scanner scanner = new Scanner(input);
        panda.getLogger().info("Panda " + PandaConstants.VERSION + " REPL");

        Repl repl = Repl.creator(panda)
                .withCustomExceptionListener(simplified ? (exception, runtime) -> panda.getLogger().error(exception.getMessage()) : null)
                .create();

        while (scanner.hasNextLine()) {
            Collection<ReplResult> results = repl.evaluate(scanner.nextLine());
            ReplUtils.print(panda, results);
        }

        panda.getLogger().info("REPL has been terminated");
    }

}
