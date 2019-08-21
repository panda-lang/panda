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

package org.panda_lang.panda.framework.language.resource.container;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.architecture.dynamic.AbstractExecutableStatement;

import java.util.HashMap;
import java.util.Map;

final class TryCatch extends AbstractExecutableStatement {

    private final Container tryContainer;
    private final Container finallyContainer;
    private final Map<Class<? extends Throwable>, Data> catchContainers = new HashMap<>();

    public TryCatch(Container tryContainer, Container finallyContainer) {
        this.tryContainer = tryContainer;
        this.finallyContainer = finallyContainer;
    }

    @Override
    public void execute(Flow flow) {
        try {
            flow.call(tryContainer.getStatementCells());
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

            flow.getCurrentScope().set(catchData.pointer, throwable);
            flow.call(catchData.container.getStatementCells());
        } finally {
            flow.call(finallyContainer.getStatementCells());
        }
    }

    public TryCatch addHandler(Class<? extends Throwable> type, Variable variable, int variablePointer, Container container) {
        catchContainers.put(type, new Data(variable, variablePointer, container));
        return this;
    }

    private static class Data {

        private Variable variable;
        private int pointer;
        private Container container;

        public Data(Variable variable, int pointer, Container container) {
            this.variable = variable;
            this.pointer = pointer;
            this.container = container;
        }

    }

}
