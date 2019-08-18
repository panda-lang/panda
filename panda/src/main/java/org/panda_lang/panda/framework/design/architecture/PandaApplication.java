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

package org.panda_lang.panda.framework.design.architecture;

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.runtime.Process;
import org.panda_lang.panda.framework.language.resource.head.MainScope;
import org.panda_lang.panda.framework.language.runtime.PandaProcess;
import org.panda_lang.panda.utilities.commons.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PandaApplication implements Application {

    private final Environment environment;
    private final List<Script> scripts = new ArrayList<>();

    public PandaApplication(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void launch(String... args) {
        List<MainScope> mains = scripts.stream()
                .map(script -> script.select(MainScope.class))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (mains.isEmpty()) {
            throw new PandaFrameworkException("Main statement not found");
        }

        if (mains.size() > 1) {
            throw new PandaFrameworkException("Duplicated main statement");
        }

        PandaFramework.getLogger().debug("[PandaApp] Launching application...");
        Process process = new PandaProcess(this, mains.get(0), args);

        long initTime = System.nanoTime();
        process.execute();

        long uptime = System.nanoTime() - initTime;
        PandaFramework.getLogger().debug("[PandaApp] Done (" + TimeUtils.toMilliseconds(uptime) + ")");
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    @Override
    public List<? extends Script> getScripts() {
        return scripts;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
