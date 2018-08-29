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

package org.panda_lang.panda.framework.language.runtime;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.dynamic.StandaloneExecutable;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCaller;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlow;

import java.util.Collection;

public class PandaExecutableBranch implements ExecutableBranch {

    private final PandaExecutableProcess process;
    private final ScopeInstance currentScope;
    private PandaControlFlow currentFlow;
    private Value returnedValue;
    private boolean interrupted;
    private Value instance;

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
        if (isInterrupted()) {
            return currentFlow;
        }

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
        ScopeInstance scope = standaloneScope ? (ScopeInstance) executable : currentScope;

        ExecutableBranch branch = new PandaExecutableBranch(process, scope);
        branch.instance(instance);

        if (isInterrupted()) {
            return branch;
        }

        if (standaloneScope) {
            branch.call();
        }
        else {
            executable.execute(branch);
        }

        return branch;
    }

    @Override
    public ExecutableBranch duplicate() {
        PandaExecutableBranch duplicatedBranch = new PandaExecutableBranch(process, currentScope);
        duplicatedBranch.currentFlow = this.currentFlow;
        duplicatedBranch.instance = this.instance;
        duplicatedBranch.returnedValue = this.returnedValue;
        duplicatedBranch.interrupted = this.interrupted;
        return duplicatedBranch;
    }

    @Override
    public void instance(Value instance) {
        this.instance = instance;
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
    public void setReturnValue(Value value) {
        this.returnedValue = value;
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

    @Override
    public Value getInstance() {
        return instance;
    }

}
