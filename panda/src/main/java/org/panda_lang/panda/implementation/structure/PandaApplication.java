/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.structure;

import org.panda_lang.framework.memory.Memory;
import org.panda_lang.framework.structure.Application;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.framework.structure.dynamic.WrapperInstance;
import org.panda_lang.panda.implementation.memory.PandaMemory;
import org.panda_lang.panda.language.structure.main.Main;

import java.util.ArrayList;
import java.util.List;

public class PandaApplication implements Application {

    private final Memory memory;
    private final List<Script> scripts;
    private String workingDirectory;

    public PandaApplication() {
        this.memory = new PandaMemory();
        this.scripts = new ArrayList<>();
    }

    @Override
    public void launch(String[] arguments) {
        for (Script script : scripts) {
            List<Main> mains = script.select(Main.class);

            if (mains.size() == 1) {
                Main main = mains.get(0);
                WrapperInstance instance = main.createInstance();
                // TODO
            }
            else if (mains.size() > 1) {

            }
        }
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public List<Script> getScripts() {
        return scripts;
    }

    @Override
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

}
