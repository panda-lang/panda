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

package org.panda_lang.panda.language.runtime;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.dynamic.ScopeInstance;
import org.panda_lang.panda.core.structure.util.StatementCell;
import org.panda_lang.panda.core.structure.value.Value;

import java.util.Collection;

public class PandaExecutableBranch implements ExecutableBranch {

    private final PandaExecutableProcess process;
    private final ScopeInstance currentScope;
    private Value returnedValue;
    private boolean interrupted;

    public PandaExecutableBranch(PandaExecutableProcess process, ScopeInstance currentScope) {
        this.process = process;
        this.currentScope = currentScope;
    }

    @Override
    public void call() {
        if (isInterrupted()) {
            return;
        }

        currentScope.execute(this);
    }

    @Override
    public void call(Collection<StatementCell> cells) {
        for (StatementCell statementCell : cells) {
            if (isInterrupted()) {
                return;
            }

            if (statementCell.isExecutable()) {
                Executable executable = (Executable) statementCell.getStatement();
                call(executable);
            }
        }
    }


    @Override
    public ExecutableBranch call(Executable executable) {
        if (isInterrupted()) {
            return this;
        }

        if (executable instanceof ScopeInstance) {
            return callStandalone(executable);
        }

        executable.execute(this);
        return this;
    }

    @Override
    public ExecutableBranch callStandalone(Executable executable) {
        ExecutableBranch branch = new PandaExecutableBranch(process, executable instanceof ScopeInstance ? (ScopeInstance) executable : currentScope);
        branch.call();
        return branch;
    }

    @Override
    public void interrupt() {
        this.interrupted = true;
    }

    @Override
    public void returnValue(Value value) {
        this.returnedValue = value;
        interrupt();
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public Value getReturnedValue() {
        return returnedValue;
    }

    @Override
    public ScopeInstance getCurrentScope() {
        return currentScope;
    }

}
