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

package panda.interpreter.architecture;

import panda.interpreter.PandaFrameworkException;
import panda.interpreter.architecture.packages.Script;
import panda.interpreter.architecture.statement.Main;
import panda.interpreter.runtime.PandaProcess;
import panda.interpreter.runtime.Process;
import panda.std.Option;
import panda.std.Result;

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
    public <T> Result<Option<T>, Throwable> launch(String... args) {
        Process process = new PandaProcess(this, selectMain(), args);

        try {
            return Result.ok(Option.of(process.execute()));
        } catch (Throwable throwable) {
            environment.getLogger().exception(throwable);
            return Result.error(throwable);
        }
    }

    private Main selectMain() {
        List<Main> mains = scripts.stream()
                .map(applicationScript -> applicationScript.select(Main.class))
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
