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

package org.panda_lang.panda.framework.design.runtime.flow;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeFrame;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Process;
import org.panda_lang.panda.framework.language.runtime.flow.PandaControlFlowController;

import java.util.Collection;

public interface Flow {

    /**
     * Call the current scope
     */
    void call();

    /**
     * Call a collection of statements
     *
     * @param cells a collection of statement cells
     */
    void call(Collection<? extends StatementCell> cells);

    /**
     * Call a collection of statements by ControlFlow
     *
     * @param cells  a collection of statement cells
     * @param caller a flow caller
     */
    ControlFlow callFlow(Collection<? extends StatementCell> cells, PandaControlFlowController caller);

    /**
     * Call single executable by {@link org.panda_lang.panda.framework.design.runtime.Process}
     *
     * @param executable an executable to call
     * @return the parent branch of called executable
     */
    Flow call(Executable executable);

    /**
     * Call single executable in dedicated branch
     *
     * @param executable an executable to call
     * @return the parent branch of called executable
     */
    Flow callStandalone(Executable executable);

    /**
     * @return an independent duplicate of branch
     */
    Flow duplicate();

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
     * @return the passed value
     */
    Value setReturnValue(Value value);

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
    ScopeFrame getCurrentScope();

    /**
     * @return current object
     */
    Value getInstance();

    /**
     * Get associated process
     *
     * @return the process
     */
    Process getProcess();

}
