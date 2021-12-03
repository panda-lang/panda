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

package panda.manager;

import net.dzikoysk.cdn.CdnFactory;
import net.dzikoysk.cdn.source.Source;
import panda.interpreter.FrameworkController;
import panda.interpreter.logging.Logger;
import panda.interpreter.logging.LoggerHolder;
import panda.manager.goals.Install;
import panda.manager.goals.Run;
import panda.std.Option;
import panda.std.function.ThrowingFunction;

import java.io.File;

public final class PackageManager implements LoggerHolder {

    private final FrameworkController controller;
    private final File workingDirectory;

    public PackageManager(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
    }

    public void install(File documentFile) throws Exception {
        Install install = new Install(this, readPackageInfo(documentFile));
        install.run();
    }

    public Option<Object> run(File documentFile) throws Exception {
        return new Run().run(this, readPackageInfo(documentFile));
    }

    public PackageInfo readPackageInfo(File documentFile) throws Exception {
        return CdnFactory.createStandard()
                .load(Source.of(documentFile), PackageDocument.class)
                .map(document -> new PackageInfo(documentFile, document))
                .orElseThrow(ThrowingFunction.identity());
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public FrameworkController getFrameworkController() {
        return controller;
    }

    @Override
    public Logger getLogger() {
        return controller.getLogger();
    }

}
