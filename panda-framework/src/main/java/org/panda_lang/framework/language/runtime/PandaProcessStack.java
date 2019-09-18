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

package org.panda_lang.framework.language.runtime;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledBlock;
import org.panda_lang.framework.design.architecture.dynamic.Controller;
import org.panda_lang.framework.design.architecture.dynamic.Executable;
import org.panda_lang.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.architecture.statement.Cell;
import org.panda_lang.framework.design.architecture.statement.Statement;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;

public class PandaProcessStack implements ProcessStack {

    private final Process process;
    private LivingFrame currentFrame;

    public PandaProcessStack(Process process) {
        this.process = process;
    }

    @Override
    public @Nullable Result<?> call(Object instance, LivingFrame frame) {
        LivingFrame cachedFrame = this.currentFrame;
        this.currentFrame = frame;

        Result<?> result = call(frame, frame.getFrame());

        this.currentFrame = cachedFrame;
        return result;
    }

    @Override
    public @Nullable Result<?> call(Object instance, Scope scope) {
        for (Cell cell : scope.getCells()) {
            Result<?> result = call(instance, cell);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Override
    public @Nullable Result<?> call(Object instance, Cell cell) {
        Statement statement = cell.getStatement();

        if (cell.isExecutable()) {
            if (cell.isController()) {
                Controller controller = (Controller) statement;
                return new Result<>(controller.getStatus(), controller.execute(this, instance));
            }

            ((Executable) statement).execute(this, instance);
            return null;
        }

        if (cell.isScope()) {
            if (cell.isControlledBlock()) {
                return ((ControlledBlock) statement).controlledCall(this, instance);
            }

            return call(instance, (Scope) statement);
        }

        return null;
    }

    @Override
    public LivingFrame getCurrentScope() {
        return currentFrame;
    }

    @Override
    public Process getProcess() {
        return process;
    }

}
