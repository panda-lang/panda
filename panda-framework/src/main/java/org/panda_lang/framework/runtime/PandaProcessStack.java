/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.runtime;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.architecture.dynamic.Controller;
import org.panda_lang.framework.architecture.dynamic.Executable;
import org.panda_lang.framework.architecture.dynamic.Frame;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.architecture.statement.Statement;
import org.panda_lang.utilities.commons.collection.FixedStack;
import org.panda_lang.utilities.commons.collection.IStack;
import panda.std.function.ThrowingSupplier;

public final class PandaProcessStack implements ProcessStack {

    private final Process process;
    private final IStack<Statement> stack;
    private Frame currentFrame;

    public PandaProcessStack(Process process, int stackCapacity) {
        this.process = process;
        this.stack = new FixedStack<>(stackCapacity);
    }

    @Override
    public @Nullable Result<?> callFrame(Object instance, Frame frame) throws Exception {
        return callCustomFrame(instance, frame, () -> callScope(frame, frame.getFramedScope()));
    }

    @Override
    public @Nullable Result<?> callCustomFrame(Object instance, Frame frame, ThrowingSupplier<Result<?>, Exception> resultSupplier) throws Exception {
        Frame cachedFrame = currentFrame;
        this.currentFrame = frame;

        Result<?> result = resultSupplier.get();

        this.currentFrame = cachedFrame;
        return result;
    }

    @Override
    public @Nullable Result<?> callStatement(Object instance, Statement statement) throws Exception {
        stack.push(statement);
        Result<?> result = callInternal(instance, statement);
        stack.pop();
        return result;
    }

    private @Nullable Result<?> callInternal(Object instance, Statement statement) throws Exception {
        if (statement instanceof Scope) {
            if (statement instanceof ControlledScope) {
                return ((ControlledScope) statement).controlledCall(this, instance);
            }

            return callScope(instance, (Scope) statement);
        }

        if (statement instanceof Executable) {
            if (statement instanceof Controller) {
                Controller controller = (Controller) statement;
                return new Result<>(controller.getStatusCode(), controller.execute(this, instance));
            }

            ((Executable) statement).execute(this, instance);
            return null;
        }

        return null;
    }

    @Override
    public @Nullable Result<?> callScope(Object instance, Scope scope) throws Exception {
        for (Statement statement : scope.getStatements()) {
            Result<?> result = callStatement(instance, statement);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Override
    public Statement[] getLivingFramesOnStack() {
        return stack.toArray(Statement[].class);
    }

    @Override
    public Frame getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public Process getProcess() {
        return process;
    }

}
