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
import org.panda_lang.panda.core.structure.dynamic.StandaloneExecutable;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.core.structure.wrapper.StatementCell;
import org.panda_lang.panda.language.runtime.flow.ControlFlow;
import org.panda_lang.panda.language.runtime.flow.ControlFlowCaller;
import org.panda_lang.panda.language.runtime.flow.PandaControlFlow;

import java.util.Collection;

public class PandaExecutableBranch implements ExecutableBranch {

    private final PandaExecutableProcess process;
    private final ScopeInstance currentScope;
    private PandaControlFlow currentFlow;
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
    public ControlFlow callFlow(Collection<StatementCell> cells, ControlFlowCaller caller) {
        PandaControlFlow parentFlow = currentFlow;

        this.currentFlow = new PandaControlFlow(this, cells, caller);
        currentFlow.execute(this);

        this.currentFlow = parentFlow;
        return currentFlow;
    }

    @Override
    public ExecutableBranch call(Executable executable) {
        if (isInterrupted()) {
            return this;
        }

        if (executable instanceof StandaloneExecutable) {
            return callStandalone(executable);
        }

        executable.execute(this);
        return this;
    }

    @Override
    public ExecutableBranch callStandalone(Executable executable) {
        boolean standaloneScope = executable instanceof ScopeInstance;
        ExecutableBranch branch = new PandaExecutableBranch(process, standaloneScope ? (ScopeInstance) executable : currentScope);

        if (standaloneScope) {
            branch.call();
        }
        else {
            executable.execute(branch);
        }

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

    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public Value getReturnedValue() {
        return returnedValue;
    }

    @Override
    public ControlFlow getCurrentControlFlow() {
        return currentFlow;
    }

    @Override
    public ScopeInstance getCurrentScope() {
        return currentScope;
    }

}
