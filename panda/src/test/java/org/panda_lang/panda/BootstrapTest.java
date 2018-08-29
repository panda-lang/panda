/*
 * Copyright (c) 2015-2018 Dzikoysk
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

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.bootstrap.PandaApplicationBootstrap;
import org.panda_lang.panda.bootstrap.PandaBootstrap;
import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.PandaFrameworkLogger;
import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.framework.language.architecture.prototype.generator.ClassPrototypeGenerator;
import org.panda_lang.panda.framework.language.architecture.prototype.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.panda.framework.language.architecture.prototype.registry.ClassPrototypeModel;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;
import org.panda_lang.panda.utilities.commons.objects.TimeUtils;

public class BootstrapTest {

    @Test
    public void testBootstraps() {
        PandaFrameworkLogger.printJVMUptime();

        for (int i = 0; i < 1; i++) {
            System.out.println("BootstrapTest Loop: " + i);
            this.testBootstrap();
        }
    }

    private void testBootstrap() {
        Panda panda = new PandaBootstrap()
                .syntax(PandaSyntax.getInstance())
                .mapModels(ClassPrototypeModel.of("panda-lang"))
                .get();

        Application application = new PandaApplicationBootstrap(panda)
                .source("../examples/current_test.panda")
                .createApplication();

        if (application == null) {
            PandaFramework.getLogger().warn("Interpretation failed, application does not exist");
            return;
        }

        application.launch();
        PandaFramework.getLogger().debug("Reflections time: " + TimeUtils.toMilliseconds(ClassPrototypeGeneratorManager.getReflectionsTime()));

        ClassPrototypeGenerator.resetLoadTime();
        ClassPrototypeGeneratorManager.resetReflectionsTime();
    }

}
