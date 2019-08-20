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

package org.panda_lang.panda.framework.language.resource.expression.subparsers;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.runtime.expression.DynamicExpression;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.expression.subparsers.number.NumberPriorities;

public class CreaseExpression extends NumberPriorities implements DynamicExpression {

    private final Accessor<?> accessor;
    private final boolean grow;
    private final boolean post;

    public CreaseExpression(Accessor<?> accessor, boolean grow, boolean post) {
        this.accessor = accessor;
        this.grow = grow;
        this.post = post;
    }

    @Override
    public Value call(Expression expression, Flow flow) {
        Value before = accessor.fetchMemoryContainer(flow).get(accessor.getMemoryPointer());
        Value after = accessor.perform(flow, (accessor, currentBranch, currentValue) -> new PandaStaticValue(currentValue.getType(), of(currentValue)));
        return post ? after : before;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private Object of(Value value) {
        switch (getPriority(value.getType())) {
            case BYTE:
                Byte byteValue = value.getValue();
                return grow ? ++byteValue : --byteValue;
            case SHORT:
                Short shortValue = value.getValue();
                return grow ? ++shortValue : --shortValue;
            case INT:
                Integer intValue = value.getValue();
                return grow ? ++intValue : --intValue;
            case LONG:
                Long longValue = value.getValue();
                return grow ? ++longValue : --longValue;
            case FLOAT:
                Float floatValue = value.getValue();
                return grow ? ++floatValue : --floatValue;
            case DOUBLE:
                Double doubleValue = value.getValue();
                return grow ? ++doubleValue : --doubleValue;
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
