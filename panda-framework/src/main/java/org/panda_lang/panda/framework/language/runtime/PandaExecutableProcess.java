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

package org.panda_lang.panda.framework.language.runtime;

import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableProcess;

public class PandaExecutableProcess implements ExecutableProcess {

    private final Application application;
    private final Scope mainScope;
    private final String[] parameters;

    public PandaExecutableProcess(Application application, Scope mainScope, String... parameters) {
        this.application = application;
        this.mainScope = mainScope;
        this.parameters = parameters;
    }

    @Override
    public Value execute() {
        ScopeInstance instance = mainScope.createInstance(null); // TODO: check behaviour of branch after applying the 'null' value

        PandaExecutableBranch branch = new PandaExecutableBranch(this, instance);
        branch.call();

        return branch.getReturnedValue();
    }

    public String[] getParameters() {
        return parameters;
    }

    public Scope getMainScope() {
        return mainScope;
    }

    public Application getApplication() {
        return application;
    }

}
