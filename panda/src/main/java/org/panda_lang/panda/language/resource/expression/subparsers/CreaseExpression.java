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

package org.panda_lang.panda.language.resource.expression.subparsers;

import org.panda_lang.panda.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.language.architecture.dynamic.accessor.AccessorVisitor;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.resource.expression.subparsers.number.NumberPriorities;
import org.panda_lang.panda.language.runtime.expression.DynamicExpression;

public class CreaseExpression extends NumberPriorities implements DynamicExpression, AccessorVisitor {

    private final Accessor<?> accessor;
    private final boolean grow;
    private final boolean post;
    private final int priority;

    public CreaseExpression(Accessor<?> accessor, boolean grow, boolean post) {
        this.accessor = accessor;
        this.grow = grow;
        this.post = post;
        this.priority = getPriority(accessor.getTypeReference().fetch());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object call(Expression expression, Flow flow) {
        Object before = accessor.fetchMemoryContainer(flow).get(accessor.getMemoryPointer());
        Object after = accessor.perform(flow, this);
        return post ? after : before;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T visit(Accessor<?> accessor, Flow flow, T currentValue) {
        return (T) of(priority, currentValue);
    }

    private Object of(int priority, Object value) {
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
    public ClassPrototype getReturnType() {
        return accessor.getVariable().getType().fetch();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
