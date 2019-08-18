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

package org.panda_lang.panda.framework.language.resource.container.assignation.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractStatement;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.runtime.expression.FunctionalDynamicExpression;

public class ArrayValueAccessor extends AbstractStatement implements Executable {

    private final ArrayValueAccessorAction action;
    private final ArrayClassPrototype prototype;
    private final ClassPrototype type;
    private final Expression instance;
    private final Expression index;

    public ArrayValueAccessor(ArrayClassPrototype prototype, ClassPrototype type, Expression instance, Expression index, ArrayValueAccessorAction action) {
        if (!instance.getReturnType().isArray()) {
            throw new IllegalArgumentException("The specified instance is not an array");
        }

        if (!PandaTypes.INT.isAssignableFrom(index.getReturnType())) {
            throw new IllegalArgumentException("The specified index is not an integer");
        }

        this.prototype = prototype;
        this.type = type;
        this.instance = instance;
        this.index = index;
        this.action = action.initialize(this);
    }

    @Override
    public void execute(Flow flow) {
        perform(flow);
    }

    public @Nullable PandaStaticValue perform(Flow flow) {
        Object[] array = instance.evaluate(flow).getValue();
        Integer i = index.evaluate(flow).getValue();
        return action.perform(flow, prototype, type, array, i);
    }

    public FunctionalDynamicExpression toCallback() {
        return new FunctionalDynamicExpression(getReturnType(), this::perform);
    }

    public interface ArrayValueAccessorAction {

        default ArrayValueAccessorAction initialize(ArrayValueAccessor accessor) {
            return this;
        }

        @Nullable PandaStaticValue perform(Flow flow, ArrayClassPrototype prototype, ClassPrototype type, Object[] array, int index);

        default @Nullable ClassPrototype getType() {
            return null;
        }

    }

    public ClassPrototype getReturnType() {
        return action.getType() == null ? type : action.getType();
    }

}
