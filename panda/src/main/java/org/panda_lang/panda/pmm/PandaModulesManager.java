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

import org.panda_lang.framework.design.interpreter.messenger.Messenger;

import java.io.File;
import java.io.IOException;

public final class PandaModulesManager {

    private final Messenger messenger;
    private final File workingDirectory;

    public PandaModulesManager(Messenger messenger, File workingDirectory) {
        this.messenger = messenger;
        this.workingDirectory = workingDirectory;
    }

    public void install(File documentFile) throws IOException {
        ModuleDocumentFile moduleDocumentFile = new ModuleDocumentFile(documentFile);
        ModuleDocument document = moduleDocumentFile.getContent();

        Install install = new Install(this, document);
        install.run();
    }

    protected Messenger getMessenger() {
        return messenger;
    }

}
