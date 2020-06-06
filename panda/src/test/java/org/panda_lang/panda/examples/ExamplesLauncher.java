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

package org.panda_lang.panda.examples;

import org.junit.jupiter.api.Assertions;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;
import org.panda_lang.panda.bootstrap.PandaApplicationBootstrap;
import org.panda_lang.panda.util.PandaUtils;
import org.panda_lang.utilities.commons.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class ExamplesLauncher {

    public static void launch(String directory, String file) {
        Assertions.assertDoesNotThrow(() -> {
            Application application = interpret(directory, file);
            launch(application);
        });
    }

    public static void launch(Application application) {
        Assertions.assertDoesNotThrow(() -> {
            application.getLogger().debug("[PandaApp] Launching application...");
            long initTime = System.nanoTime();

            Object result = application.launch();

            long uptime = System.nanoTime() - initTime;
            String uptimeValue = " (" + TimeUtils.toMilliseconds(uptime) + ")";

            if (result instanceof Integer) {
                application.getLogger().debug("[PandaApp] Done, process finished with exit code " + result + uptimeValue);
                return;
            }

            application.getLogger().debug("[PandaApp] Done" + uptimeValue);
        });
    }

    public static Application interpret(String directory, String file) {
        Logger logger = LoggerFactory.getLogger(ExamplesLauncher.class);
        PandaUtils.printJVMUptime(() -> logger);

        PandaFactory factory = new PandaFactory();
        Panda panda = factory.createPanda(logger);

        Optional<Application> application = PandaApplicationBootstrap.create(panda)
                .workingDirectory("../examples/" + directory)
                .script(file)
                .createApplication();

        Assertions.assertTrue(application.isPresent());
        return application.get();
    }

}
