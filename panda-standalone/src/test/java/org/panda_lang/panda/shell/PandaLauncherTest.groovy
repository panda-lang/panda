/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.shell

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test
import org.panda_lang.framework.interpreter.logging.DefaultLogger
import org.panda_lang.framework.interpreter.logging.Logger
import org.panda_lang.panda.PandaConstants

import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class PandaLauncherTest {

    private StringBuilder output = new StringBuilder()
    private Logger logger = new DefaultLogger({ String message -> output.append(message) })

    @Test
    void 'should print help' () {
        PandaLauncher.launch(() -> logger, System.in, '--help')
        assertTrue output.contains('Usage')
    }

    @Test
    void 'should parse command line parameters' () {
        PandaLauncher.launch(() -> logger, System.in, '--version')
        assertTrue output.contains(PandaConstants.VERSION)
    }

}
