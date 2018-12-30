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

import org.panda_lang.panda.Panda;
import picocli.CommandLine;

public class PandaCLI {

    private final Panda panda;

    public PandaCLI(Panda panda) {
        this.panda = panda;
    }

    public void run(String... args) {
        CommandLine.run(new PandaCommand(this), args);
    }

    public Panda getPanda() {
        return panda;
    }

}