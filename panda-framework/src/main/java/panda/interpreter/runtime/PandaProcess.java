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

package panda.interpreter.runtime;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.Application;
import panda.interpreter.architecture.dynamic.Frame;
import panda.interpreter.architecture.statement.StandardizedFramedScope;

public final class PandaProcess implements Process {

    private final Application application;
    private final String[] parameters;
    private final StandardizedFramedScope mainScope;

    public PandaProcess(Application application, StandardizedFramedScope mainScope, String... parameters) {
        this.application = application;
        this.mainScope = mainScope;
        this.parameters = parameters;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable Object execute() {
        ProcessStack stack = new PandaProcessStack(this, PandaRuntimeConstants.DEFAULT_STACK_SIZE);

        try {
            Frame instance = mainScope.revive(null, null);
            Result<?> result = stack.callFrame(instance, instance);
            return result != null ? result.getResult() : null;
        } catch (Exception exception) {
            throw new PandaProcessFailure(stack, exception);
        }
    }

    public String[] getParameters() {
        return parameters;
    }

    public Application getApplication() {
        return application;
    }

}
