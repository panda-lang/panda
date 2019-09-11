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

package org.panda_lang.panda.language.architecture.dynamic.accessor;

import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.language.architecture.dynamic.assigner.FieldAssigner;

import java.util.function.Function;

public class FieldAccessor extends DefaultAccessor<PrototypeField> {

    public FieldAccessor(Function<Flow, MemoryContainer> memory, PrototypeField field, int internalPointer) {
        super(memory, field, internalPointer);
    }

    public FieldAccessor(Expression instance, PrototypeField field) {
        this(new FieldAccessorFunction(instance), field, field.getFieldIndex());
    }

    @Override
    public Assigner<PrototypeField> toAssigner(Expression value) {
        return new FieldAssigner(this, value);
    }

}
