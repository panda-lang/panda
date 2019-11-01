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

package org.panda_lang.panda.manager;

import org.junit.jupiter.api.Test;
import org.panda_lang.framework.language.interpreter.messenger.PandaMessenger;
import org.panda_lang.panda.util.PandaUtils;
import org.slf4j.LoggerFactory;

import java.io.File;

class ModuleManagerTest {

    private static final File DIRECTORY = new File("../examples/manager/");
    private static final ModuleManager MANAGER = new ModuleManager(new PandaMessenger(LoggerFactory.getLogger(ModuleManagerTest.class)), DIRECTORY);

    @Test
    void test() throws Exception {
        PandaUtils.printJVMUptime(MANAGER.getMessenger());
        File document = new File(DIRECTORY, "panda.hjson");

        MANAGER.install(document);
        MANAGER.run(document);
    }

}