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

package org.panda_lang.panda.framework.design.architecture;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.language.architecture.AbstractScript;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.framework.language.interpreter.parsers.overall.imports.PandaImportRegistry;

public class PandaScript extends AbstractScript {

    protected final ImportRegistry importRegistry;
    protected Module currentModule;

    public PandaScript(String scriptName) {
        super(scriptName);
        this.importRegistry = new PandaImportRegistry();
    }

    public void setModule(Module currentModule) {
        this.currentModule = currentModule;
    }

    public Module getModule() {
        return currentModule;
    }

    public ImportRegistry getImportRegistry() {
        return importRegistry;
    }

}
