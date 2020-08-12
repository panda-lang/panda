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
import org.panda_lang.language.architecture.dynamic.Frame;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.runtime.Result;
import org.panda_lang.language.architecture.statement.AbstractBlock;

import java.util.Iterator;

final class ForEachBlock extends AbstractBlock implements ControlledScope {

    private final int valuePointer;
    private final Expression iterableExpression;

    ForEachBlock(Scope parent, Location location, Expression iterableExpression) {
        super(parent, location);
        this.iterableExpression = iterableExpression;
        this.valuePointer = getFramedScope().allocate();
    }

    @Override
    public @Nullable Result<?> controlledCall(ProcessStack stack, Object instance) throws Exception {
        Frame scope = stack.getCurrentFrame();
        Iterable<?> iterable = iterableExpression.evaluate(stack, instance);
        Iterator<?> iterator = iterable.iterator();

        return new ControlledIteration(() -> {
            if (!iterator.hasNext()) {
                return false;
            }

            scope.set(valuePointer, iterator.next());
            return true;
        }).iterate(stack, instance, this);
    }

    public Expression getIterableExpression() {
        return iterableExpression;
    }

    public int getValuePointer() {
        return valuePointer;
    }

}
