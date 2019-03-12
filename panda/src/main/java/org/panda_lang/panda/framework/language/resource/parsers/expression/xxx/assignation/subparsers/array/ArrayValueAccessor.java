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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Executable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.statement.AbstractStatement;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.runtime.expression.FunctionalExpressionCallback;

public class ArrayValueAccessor extends AbstractStatement implements Executable {

    private final ArrayValueAccessorAction action;
    private final ArrayClassPrototype prototype;
    private final ClassPrototype type;
    private final Expression instance;
    private final Expression index;

    public ArrayValueAccessor(ArrayClassPrototype prototype, ClassPrototype type, Expression instance, Expression index, ArrayValueAccessorAction action) {
        this.prototype = prototype;
        this.type = type;
        this.instance = instance;
        this.index = index;
        this.action = action.initialize(this);
    }

    @Override
    public void execute(ExecutableBranch branch) {
        perform(branch);
    }

    public @Nullable PandaValue perform(ExecutableBranch branch) {
        Object[] array = instance.getExpressionValue(branch).getValue();
        Number i = index.getExpressionValue(branch).getValue();
        return action.perform(branch, prototype, type, array, i);
    }

    public FunctionalExpressionCallback toCallback() {
        return new FunctionalExpressionCallback(getReturnType(), this::perform);
    }

    public interface ArrayValueAccessorAction {

        default ArrayValueAccessorAction initialize(ArrayValueAccessor accessor) {
            return this;
        }

        @Nullable PandaValue perform(ExecutableBranch branch, ArrayClassPrototype prototype, ClassPrototype type, Object[] array, Number index);

        default @Nullable ClassPrototype getType() {
            return null;
        }

    }

    public ClassPrototype getReturnType() {
        return action.getType() == null ? type : action.getType();
    }

}
