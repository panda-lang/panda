/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.structure;

import org.panda_lang.panda.implementation.structure.dynamic.Executable;
import org.panda_lang.framework.structure.Statement;

public class PandaStatementCell implements StatementCell {

    private Statement statement;
    private boolean manipulated;

    public PandaStatementCell(Statement executable) {
        this.statement = executable;
    }

    @Override
    public void setStatement(Statement executable) {
        if (this.statement != null) {
            this.manipulated = true;
        }

        this.statement = executable;
    }

    public boolean isManipulated() {
        return manipulated;
    }

    @Override
    public boolean isExecutable() {
        return statement != null && statement instanceof Executable;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

}
