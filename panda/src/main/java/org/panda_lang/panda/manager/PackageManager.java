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

package org.panda_lang.panda.manager;

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.interpreter.logging.Logger;
import org.panda_lang.framework.interpreter.logging.LoggerHolder;
import org.panda_lang.utilities.commons.function.Option;

import java.io.File;
import java.io.IOException;

public final class PackageManager implements LoggerHolder {

    private final FrameworkController controller;
    private final File workingDirectory;

    public PackageManager(FrameworkController controller, File workingDirectory) {
        this.controller = controller;
        this.workingDirectory = workingDirectory;
    }

    public void install(File documentFile) throws IOException {
        Install install = new Install(this, new PackageDocumentFile(documentFile).getContent());
        install.run();
    }

    public Option<Object> run(File documentFile) throws IOException {
        return run(controller, documentFile);
    }

    public Option<Object> run(FrameworkController controller, File documentFile) throws IOException {
        Run run = new Run(this, new PackageDocumentFile(documentFile).getContent());
        return run.run(controller);
    }

    protected File getWorkingDirectory() {
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
