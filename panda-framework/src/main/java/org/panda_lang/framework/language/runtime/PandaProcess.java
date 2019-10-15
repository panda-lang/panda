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

package org.panda_lang.framework.language.runtime;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.statement.FramedScope;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;

public class PandaProcess implements Process {

    private final Application application;
    private final String[] parameters;
    private final FramedScope mainScope;

    public PandaProcess(Application application, FramedScope mainScope, String... parameters) {
        this.application = application;
        this.mainScope = mainScope;
        this.parameters = parameters;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object execute() {
        ProcessStack stack = new PandaProcessStack(this, PandaProcessStackConstants.DEFAULT_STACK_SIZE);

        try {
            Frame instance = mainScope.revive(null, null);
            Result<?> result = stack.call(instance, instance);
            return result != null ? result.getResult() : null;
        } catch (Exception e) {
            application.getEnvironment().getMessenger().send(new PandaProcessFailure(stack, e));
            return -1;
        }
    }

    public String[] getParameters() {
        return parameters;
    }

    public Application getApplication() {
        return application;
    }

}
