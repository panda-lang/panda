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

package org.panda_lang.panda.manager;

import org.panda_lang.language.FrameworkController;
import org.panda_lang.language.interpreter.messenger.Messenger;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaFactory;

import java.io.File;
import java.io.IOException;

public final class PackageManager {

    private final Messenger messenger;
    private final File workingDirectory;

    public PackageManager(Messenger messenger, File workingDirectory) {
        this.messenger = messenger;
        this.workingDirectory = workingDirectory;
    }

    public void install(File documentFile) throws IOException {
        Install install = new Install(this, new PackageDocumentFile(documentFile).getContent());
        install.run();
    }

    public void run(File documentFile) throws IOException {
        PandaFactory factory = new PandaFactory();
        Panda panda = factory.createPanda(messenger.getLogger());
        run(panda, documentFile);
    }

    public void run(FrameworkController controller, File documentFile) throws IOException {
        Run run = new Run(this, new PackageDocumentFile(documentFile).getContent());
        run.run(controller);
    }

    protected File getWorkingDirectory() {
        return workingDirectory;
    }

    protected Messenger getMessenger() {
        return messenger;
    }

}
