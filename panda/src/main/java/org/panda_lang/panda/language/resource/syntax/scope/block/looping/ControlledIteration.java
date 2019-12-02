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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledScope;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.runtime.Status;
import org.panda_lang.utilities.commons.function.ThrowingRunnable;
import org.panda_lang.utilities.commons.function.ThrowingSupplier;

final class ControlledIteration {

    private final ThrowingSupplier<Boolean, Exception> condition;
    private final ThrowingRunnable<Exception> after;

    ControlledIteration(ThrowingSupplier<Boolean, Exception> condition, @Nullable ThrowingRunnable<Exception> after) {
        this.condition = condition;
        this.after = after;
    }

    ControlledIteration(ThrowingSupplier<Boolean, Exception> condition) {
        this(condition, null);
    }

    protected Result<?> iterate(ProcessStack stack, Object instance, ControlledScope scope) throws Exception {
        while (condition.get()) {
            Result<?> result = stack.callScope(instance, scope);

            if (after != null) {
                after.run();
            }

            if (result == null || result.getStatus() == Status.CONTINUE) {
                continue;
            }

            if (result.getStatus() == Status.BREAK) {
                break;
            }

            return result;
        }

        return null;
    }

}
