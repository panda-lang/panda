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

package org.panda_lang.panda.framework.language.runtime;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.dynamic.StandaloneExecutable;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.Process;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCallback;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlow;

import java.util.Collection;

public class PandaFlow implements Flow {

    private final Process process;
    private final Value instance;
    private final ScopeFrame currentScope;

    private PandaControlFlow currentFlow;
    private Value returnedValue;
    private boolean interrupted;

    public PandaFlow(Flow flow, Value instance) {
        this(flow.getProcess(), instance, flow.getCurrentScope());
    }

    public PandaFlow(Process process, Value instance, ScopeFrame currentScope) {
        this.process = process;
        this.instance = instance;
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
    public void call(Collection<? extends StatementCell> cells) {
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
    public ControlFlow callFlow(Collection<? extends StatementCell> cells, ControlFlowCallback caller) {
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
    public Flow call(Executable executable) {
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
    public Flow callStandalone(Executable executable) {
        boolean standaloneScope = executable instanceof ScopeFrame;
        ScopeFrame scope = standaloneScope ? (ScopeFrame) executable : currentScope;

        Flow flow = new PandaFlow(process, instance, scope);

        if (isInterrupted()) {
            return flow;
        }

        if (standaloneScope) {
            flow.call();
        }
        else {
            executable.execute(flow);
        }

        return flow;
    }

    @Override
    public Flow duplicate() {
        PandaFlow duplicatedBranch = new PandaFlow(process, instance, currentScope);
        duplicatedBranch.currentFlow = this.currentFlow;
        duplicatedBranch.returnedValue = this.returnedValue;
        duplicatedBranch.interrupted = this.interrupted;
        return duplicatedBranch;
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
    public Value setReturnValue(Value value) {
        return (this.returnedValue = value);
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
    public ScopeFrame getCurrentScope() {
        return currentScope;
    }

    @Override
    public Value getInstance() {
        return instance;
    }

    @Override
    public Process getProcess() {
        return process;
    }

}
