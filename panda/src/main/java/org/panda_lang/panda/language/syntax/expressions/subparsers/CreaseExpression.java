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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.panda_lang.framework.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.framework.architecture.expression.DynamicExpression;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.interpreter.parser.PandaParserException;
import org.panda_lang.framework.runtime.MemoryContainer;
import org.panda_lang.framework.runtime.ProcessStack;
import org.panda_lang.panda.language.syntax.expressions.subparsers.number.NumberPriorities;

import java.util.function.Function;

final class CreaseExpression extends NumberPriorities implements DynamicExpression {

    private final Accessor<?> accessor;
    private final boolean grow;
    private final boolean post;
    private final Function<Number, Object> function;

    public CreaseExpression(Accessor<?> accessor, boolean grow, boolean post) {
        this.accessor = accessor;
        this.grow = grow;
        this.post = post;
        this.function = toFunction(getPriority(accessor.getKnownType()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        MemoryContainer memory = accessor.fetchMemoryContainer(stack, instance);

        Number before = memory.get(accessor.getMemoryPointer());
        Object after = function.apply(before);
        memory.set(accessor.getMemoryPointer(), after);

        return post
                ? before
                : after;
    }

    private Function<Number, Object> toFunction(int priority) {
        switch (priority) {
            case INT:
                return grow
                        ? value -> value.intValue() + 1
                        : value -> value.intValue() - 1;
            case LONG:
                return grow
                        ? value -> value.longValue() + 1L
                        : value -> value.longValue() - 1L;
            case DOUBLE:
                return grow
                        ? value -> value.doubleValue() + 1.0D
                        : value -> value.doubleValue() - 1.0D;
            case FLOAT:
                return grow
                        ? value -> value.floatValue() + 1.0F
                        : value -> value.floatValue() - 1.0F;
            case BYTE:
                return grow
                        ? value -> value.byteValue() + 1
                        : value -> value.byteValue() - 1;
            case SHORT:
                return grow
                        ? value -> value.shortValue() + 1
                        : value -> value.shortValue() - 1;
            default:
                throw new PandaParserException("Unknown number type: " + priority);
        }
    }

    @Override
    public Signature getReturnType() {
        return accessor.getVariable().getSignature();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
