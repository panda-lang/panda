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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.architecture.dynamic.AbstractExecutableStatement;

import java.util.HashMap;
import java.util.Map;

public final class TryCatch extends AbstractExecutableStatement {

    private final Scope tryBlock;
    private final Scope finallyBlock;
    private final Map<Class<? extends Throwable>, Data> catchContainers = new HashMap<>();

    public TryCatch(Location location, Scope tryBlock, Scope finallyBlock) {
        super(location);
        this.tryBlock = tryBlock;
        this.finallyBlock = finallyBlock;
    }

    @Override
    public @Nullable Object execute(ProcessStack stack, Object instance) throws Exception {
        try {
            return stack.callScope(instance, tryBlock);
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

            stack.getCurrentFrame().set(catchData.variable.getPointer(), throwable);
            stack.callScope(instance, catchData.block);
        } finally {
            stack.callScope(instance, finallyBlock);
        }

        return null;
    }

    public TryCatch addHandler(Class<? extends Throwable> type, Variable variable, Scope block) {
        catchContainers.put(type, new Data(variable, block));
        return this;
    }

    private static final class Data {

        private final Variable variable;
        private final Scope block;

        public Data(Variable variable, Scope block) {
            this.variable = variable;
            this.block = block;
        }

    }

}
