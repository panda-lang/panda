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

package org.panda_lang.panda.framework.language.runtime.flow;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlowCaller;

import java.util.Collection;

public class PandaControlFlow implements Executable, ControlFlow {

    private final Frame frame;
    private final Collection<? extends StatementCell> cells;
    private final ControlFlowCaller caller;
    private boolean skipped;
    private boolean escaped;

    public PandaControlFlow(Frame frame, Collection<? extends StatementCell> cells, ControlFlowCaller caller) {
        this.frame = frame;
        this.cells = cells;
        this.caller = caller;
    }

    @Override
    public void execute(Frame frame) {
        caller.call(frame, this);
    }

    @Override
    public void call() {
        reset();

        for (StatementCell cell : cells) {
            if (frame.isInterrupted() || isEscaped() || isSkipped()) {
                break;
            }

            if (cell.isExecutable()) {
                frame.call((Executable) cell.getStatement());
            }
        }
    }

    @Override
    public void reset() {
        this.skipped = false;
        this.escaped = false;
    }

    @Override
    public void skip() {
        this.skipped = true;
    }

    @Override
    public void escape() {
        this.escaped = true;
    }

    @Override
    public boolean isSkipped() {
        return skipped;
    }

    @Override
    public boolean isEscaped() {
        return escaped;
    }

}
