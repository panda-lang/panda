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

package org.panda_lang.panda.language.resource.scope;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.AbstractExecutableStatement;

import java.util.HashMap;
import java.util.Map;

final class TryCatch extends AbstractExecutableStatement {

    private final Scope tryScope;
    private final Scope finallyScope;
    private final Map<Class<? extends Throwable>, Data> catchContainers = new HashMap<>();

    public TryCatch(Scope tryScope, Scope finallyScope) {
        this.tryScope = tryScope;
        this.finallyScope = finallyScope;
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) {
        try {
            return stack.call(instance, tryScope);
        } catch (Throwable throwable) {
            Data catchData = catchContainers.get(throwable.getClass());

            if (catchData == null) {
                catchData = catchContainers.entrySet().stream()
                        .filter(entry -> entry.getKey().isAssignableFrom(throwable.getClass()))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElse(null);
            }

            if (catchData == null) {
                throw throwable;
            }

            stack.getCurrentScope().set(catchData.variable.getPointer(), throwable);
            stack.call(instance, catchData.scope);
        } finally {
            stack.call(instance, finallyScope);
        }

        return null;
    }

    public TryCatch addHandler(Class<? extends Throwable> type, Variable variable, Scope scope) {
        catchContainers.put(type, new Data(variable, scope));
        return this;
    }

    private static class Data {

        private Variable variable;
        private Scope scope;

        public Data(Variable variable, Scope scope) {
            this.variable = variable;
            this.scope = scope;
        }

    }

}
