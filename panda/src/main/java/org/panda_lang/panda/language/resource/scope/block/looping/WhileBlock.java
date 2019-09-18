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

package org.panda_lang.panda.language.resource.scope.block.looping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.dynamic.ControlledBlock;
import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.runtime.Status;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.architecture.dynamic.AbstractBlock;

class WhileBlock extends AbstractBlock implements ControlledBlock {

    private final Expression expression;

    WhileBlock(Scope parent, Expression expression) {
        super(parent);
        this.expression = expression;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) {
        while (expression.evaluate(stack, instance)) {
            Result<?> result = stack.call(instance, this);

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

