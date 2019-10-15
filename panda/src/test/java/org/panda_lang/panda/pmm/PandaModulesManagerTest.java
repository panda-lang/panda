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

package org.panda_lang.panda.pmm;

import org.junit.jupiter.api.Test;
import org.panda_lang.framework.language.interpreter.messenger.PandaMessenger;

import java.io.File;

class PandaModulesManagerTest {

    private static final File DIRECTORY = new File("../examples/pmm/");
    private static final PandaModulesManager MANAGER = new PandaModulesManager(new PandaMessenger(), DIRECTORY);

    @Test
    void test() throws Exception {
        File document = new File(DIRECTORY, "panda.hjson");

        MANAGER.install(document);
        MANAGER.run(document);
    }

}