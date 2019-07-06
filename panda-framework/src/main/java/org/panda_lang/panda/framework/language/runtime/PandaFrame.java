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
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCaller;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlow;

import java.util.Collection;

public class PandaFrame implements Frame {

    private static long fullUptime;

    private final PandaProcess process;
    private final ScopeFrame currentScope;
    private PandaControlFlow currentFlow;
    private Value returnedValue;
    private boolean interrupted;
    private Value instance;

    public PandaFrame(PandaProcess process, ScopeFrame currentScope) {
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
    public ControlFlow callFlow(Collection<? extends StatementCell> cells, ControlFlowCaller caller) {
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
    public Frame call(Executable executable) {
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
    public Frame callStandalone(Executable executable) {
        boolean standaloneScope = executable instanceof ScopeFrame;
        ScopeFrame scope = standaloneScope ? (ScopeFrame) executable : currentScope;

        Frame frame = new PandaFrame(process, scope);
        frame.instance(instance);

        if (isInterrupted()) {
            return frame;
        }

        if (standaloneScope) {
            frame.call();
        }
        else {
            executable.execute(frame);
        }

        return frame;
    }

    @Override
    public Frame duplicate() {
        PandaFrame duplicatedBranch = new PandaFrame(process, currentScope);
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

}
