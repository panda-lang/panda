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

package org.panda_lang.panda.framework.language.architecture;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.runtime.ExecutableProcess;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.main.MainScope;
import org.panda_lang.panda.framework.language.runtime.PandaExecutableProcess;
import org.panda_lang.panda.utilities.commons.objects.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class PandaApplication implements Application {

    protected final List<Script> scripts;
    protected String workingDirectory;
    protected String[] arguments;

    public PandaApplication() {
        this.scripts = new ArrayList<>();
    }

    @Override
    public void launch() {
        for (Script script : scripts) {
            List<MainScope> mains = script.select(MainScope.class);

            if (mains.size() == 1) {
                MainScope main = mains.get(0);
                ExecutableProcess process = new PandaExecutableProcess(this, main, this.arguments);
                PandaFramework.getLogger().info("[PandaApp] Launching application...");

                long initTime = System.nanoTime();
                process.execute();
                long uptime = System.nanoTime() - initTime;

                PandaFramework.getLogger().info("[PandaApp] Done (" + TimeUtils.toMilliseconds(uptime) + ")");
                return;
            }
            else if (mains.size() > 1) {
                throw new RuntimeException("Duplicated main statement");
            }
        }

        throw new RuntimeException("Main statement not found");
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public void setApplicationArguments(String... arguments) {
        this.arguments = arguments;
    }

    @Override
    public List<Script> getScripts() {
        return scripts;
    }

    @Override
    public String getWorkingDirectory() {
        return workingDirectory;
    }

}
