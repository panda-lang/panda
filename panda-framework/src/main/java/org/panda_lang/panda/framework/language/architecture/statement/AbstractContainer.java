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

package org.panda_lang.panda.framework.language.architecture.statement;

import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer extends AbstractStatement implements Container {

    protected final List<StatementCell> executableCells;

    public AbstractContainer() {
        this.executableCells = new ArrayList<>();
    }

    @Override
    public StatementCell reserveCell() {
        return addStatement(null);
    }

    @Override
    public StatementCell addStatement(Statement executable) {
        StatementCell executableCell = new PandaStatementCell(executable);
        executableCells.add(executableCell);
        return executableCell;
    }

    @Override
    public List<StatementCell> getStatementCells() {
        return executableCells;
    }

}
