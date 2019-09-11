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

package org.panda_lang.panda.language.runtime.flow;

import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.statement.Cell;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.flow.ControlFlow;

import java.util.Collection;

public class PandaControlFlow implements Executable, ControlFlow {

    private final Flow flow;
    private final Collection<? extends Cell> cells;
    private final PandaControlFlowController controller;
    private boolean skipped;
    private boolean escaped;

    public PandaControlFlow(Flow flow, Collection<? extends Cell> cells, PandaControlFlowController controller) {
        this.flow = flow;
        this.cells = cells;
        this.controller = controller;
    }

    @Override
    public void execute(Flow flow) {
        controller.call(this, flow);
    }

    @Override
    public void call() {
        reset();

        for (Cell cell : cells) {
            if (flow.isInterrupted() || isEscaped() || isSkipped()) {
                break;
            }

            if (cell.isExecutable()) {
                flow.call((Executable) cell.getStatement());
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
