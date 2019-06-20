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

package org.panda_lang.panda.framework.language.architecture.dynamic;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;

import java.util.HashMap;
import java.util.Map;

public final class TryCatchExecutable extends AbstractExecutableStatement {

    private final Container tryContainer;
    private final Container finallyContainer;
    private final Map<Class<? extends Throwable>, Container> catchContainers = new HashMap<>();

    public TryCatchExecutable(Container tryContainer, Container finallyContainer) {
        this.tryContainer = tryContainer;
        this.finallyContainer = finallyContainer;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        try {
            branch.call(tryContainer.getStatementCells());
        } catch (Throwable throwable) {
            Container catchContainer = catchContainers.get(throwable.getClass());

            if (catchContainer == null) {
                throw throwable;
            }

            branch.call(catchContainer.getStatementCells());
        } finally {
            branch.call(finallyContainer.getStatementCells());
        }
    }

    public TryCatchExecutable addHandler(Class<? extends Throwable> type, Container container) {
        catchContainers.put(type, container);
        return this;
    }

}
