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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.MemoryContainer;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.number.NumberPriorities;

final class CreaseExpression extends NumberPriorities implements DynamicExpression {

    private final Accessor<?> accessor;
    private final boolean grow;
    private final boolean post;
    private final int priority;

    public CreaseExpression(Accessor<?> accessor, boolean grow, boolean post) {
        this.accessor = accessor;
        this.grow = grow;
        this.post = post;
        this.priority = getPriority(accessor.getTypePrototype());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        MemoryContainer memory = accessor.fetchMemoryContainer(stack, instance);
        Object before = memory.get(accessor.getMemoryPointer());

        Object after = of(before);
        memory.set(accessor.getMemoryPointer(), after);

        return post ? after : before;
    }

    private Object of(Object value) {
        switch (priority) {
            case INT:
                int intValue = (int) value;
                return grow ? ++intValue : --intValue;
            case LONG:
                long longValue = (long) value;
                return grow ? ++longValue : --longValue;
            case DOUBLE:
                double doubleValue = (double) value;
                return grow ? ++doubleValue : --doubleValue;
            case FLOAT:
                float floatValue = (float) value;
                return grow ? ++floatValue : --floatValue;
            case BYTE:
                byte byteValue = (byte) value;
                return grow ? ++byteValue : --byteValue;
            case SHORT:
                short shortValue = (short) value;
                return grow ? ++shortValue : --shortValue;
            default:
                throw new PandaParserException("Unknown number type: " + value);
        }
    }

    @Override
    public Prototype getReturnType() {
        return accessor.getVariable().getType();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
