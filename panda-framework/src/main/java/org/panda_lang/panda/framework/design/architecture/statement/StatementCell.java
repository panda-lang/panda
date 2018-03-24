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

package org.panda_lang.panda.framework.design.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.dynamic.StandaloneExecutable;

/**
 * StatementCell is a mutable container for {@link Statement}
 */
public interface StatementCell {

    void setStatement(Statement statement);

    default boolean isBlock() {
        return getStatement() != null && getStatement() instanceof Block;
    }

    default boolean isStandaloneExecutable() {
        return getStatement() != null && getStatement() instanceof StandaloneExecutable;
    }

    default boolean isExecutable() {
        return getStatement() != null && getStatement() instanceof Executable;
    }

    default boolean isContainer() {
        return getStatement() != null && getStatement() instanceof Container;
    }

    Statement getStatement();

}
