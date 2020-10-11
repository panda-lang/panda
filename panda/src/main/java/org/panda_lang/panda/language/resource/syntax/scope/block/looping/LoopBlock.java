/*
 * Copyright (c) 2020 Dzikoysk
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
import org.panda_lang.language.architecture.dynamic.ControlledScope;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.AbstractBlock;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.source.Localizable;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Result;
import org.panda_lang.utilities.commons.function.ThrowingSupplier;

final class LoopBlock extends AbstractBlock implements ControlledScope {

    private final Expression expression;

    LoopBlock(Scope parent, Localizable localizable, Expression expression) {
        super(parent, localizable);
        this.expression = expression;
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        int times = expression.evaluate(stack, instance);

        return new ControlledIteration(new ThrowingSupplier<Boolean, Exception>() {
            private int index = 0;

            @Override
            public Boolean get() {
                return index++ < times;
            }
        }).iterate(stack, instance, this);
    }

}
