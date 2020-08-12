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

package org.panda_lang.panda.language.architecture;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.runtime.Process;
import org.panda_lang.language.runtime.PandaProcess;
import org.panda_lang.panda.language.resource.syntax.head.MainScope;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PandaApplication implements Application {

    private final Environment environment;
    private final List<Script> scripts = new ArrayList<>();

    public PandaApplication(Environment environment) {
        this.environment = environment;
    }

    @Override
    public @Nullable Object launch(String... args) {
        Process process = new PandaProcess(this, selectMain(), args);
        return process.execute();
    }

    private MainScope selectMain() {
        List<MainScope> mains = scripts.stream()
                .map(applicationScript -> applicationScript.select(MainScope.class))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (mains.isEmpty()) {
            throw new PandaFrameworkException("Main statement not found");
        }

        if (mains.size() > 1) {
            throw new PandaFrameworkException("Duplicated main statement");
        }

        return mains.get(0);
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
