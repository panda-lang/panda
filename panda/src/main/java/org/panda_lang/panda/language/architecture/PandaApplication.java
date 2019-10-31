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

package org.panda_lang.panda.language.architecture;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.panda.language.interpreter.parser.head.MainScope;
import org.panda_lang.framework.language.runtime.PandaProcess;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PandaApplication implements Application {

    private final Environment environment;
    private final List<Script> scripts = new ArrayList<>();
    private MainScope main;

    public PandaApplication(Environment environment) {
        this.environment = environment;
    }

    @Override
    public @Nullable Object launch(String... args) {
        if (main == null) {
            selectMain();
        }

        Process process = new PandaProcess(this, main, args);
        return process.execute();
    }

    private void selectMain() {
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

        this.main = mains.get(0);
    }

    public void addScript(Script script) {
        scripts.add(script);
    }

    @Override
    public Logger getLogger() {
        return environment.getLogger();
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
