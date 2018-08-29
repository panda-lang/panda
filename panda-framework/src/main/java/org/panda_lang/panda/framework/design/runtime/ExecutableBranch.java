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

package org.panda_lang.panda.framework.design.runtime;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCaller;

import java.util.Collection;

public interface ExecutableBranch {

    /**
     * Define `this` for execution process
     *
     * @param instance current object
     */
    void instance(Value instance);

    /**
     * Call the current scope
     */
    void call();

    /**
     * Call a collection of statements
     *
     * @param cells a collection of statement cells
     */
    void call(Collection<StatementCell> cells);

    /**
     * Call a collection of statements by ControlFlow
     *
     * @param cells  a collection of statement cells
     * @param caller a flow caller
     */
    ControlFlow callFlow(Collection<StatementCell> cells, ControlFlowCaller caller);

    /**
     * Call single executable by {@link ExecutableProcess}
     *
     * @param executable an executable to call
     * @return the parent branch of called executable
     */
    ExecutableBranch call(Executable executable);

    /**
     * Call single executable in dedicated branch
     *
     * @param executable an executable to call
     * @return the parent branch of called executable
     */
    ExecutableBranch callStandalone(Executable executable);

    /**
     * @return an independent duplicate of branch
     */
    ExecutableBranch duplicate();

    /**
     * Interrupt the execution process
     */
    void interrupt();

    /**
     * Interrupt the execution process and return value
     *
     * @param value result
     */
    void returnValue(Value value);

    /**
     * Return value without breaking a flow
     *
     * @param value result
     */
    void setReturnValue(Value value);

    /**
     * @return true if branch has been interrupted
     */
    boolean isInterrupted();

    /**
     * @return a returned value
     */
    Value getReturnedValue();

    /**
     * Returns null if executed code is not wrapped in ControlFlow
     *
     * @return currently executed control flow
     */
    ControlFlow getCurrentControlFlow();

    /**
     * @return instance of the current scope
     */
    ScopeInstance getCurrentScope();

    /**
     * @return current object
     */
    Value getInstance();

}
