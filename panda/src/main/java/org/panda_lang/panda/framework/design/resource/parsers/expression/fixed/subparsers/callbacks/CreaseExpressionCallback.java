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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers.callbacks;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.resource.parsers.general.number.NumberPriorities;

public class CreaseExpressionCallback extends NumberPriorities implements ExpressionCallback {

    private final Accessor<?> accessor;
    private final boolean grow;
    private final boolean post;

    public CreaseExpressionCallback(Accessor<?> accessor, boolean grow, boolean post) {
        this.accessor = accessor;
        this.grow = grow;
        this.post = post;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        Value before = accessor.fetchMemoryContainer(branch).get(accessor.getMemoryPointer());
        Value after = accessor.perform(branch, (accessor, currentBranch, currentValue) -> new PandaValue(currentValue.getType(), of(currentValue)));
        return post ? after : before;
    }

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
        return accessor.getVariable().getType();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
