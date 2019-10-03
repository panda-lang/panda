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

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;

final class Install implements ThrowingRunnable<PandaFrameworkException> {

    private final PandaModulesManager manager;
    private final ModuleDocumentFile document;

    Install(PandaModulesManager manager, ModuleDocumentFile document) {
        this.manager = manager;
        this.document = document;
    }

    public void run() throws PandaFrameworkException {

    }

}
