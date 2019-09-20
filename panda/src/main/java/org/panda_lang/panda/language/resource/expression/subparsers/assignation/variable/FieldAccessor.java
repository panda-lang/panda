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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.variable;

import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.accessor.AbstractAccessor;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;

public class FieldAccessor extends AbstractAccessor<PrototypeField> {

    public FieldAccessor(Expression instance, PrototypeField field) {
        super(new FieldAccessorFunction(instance), field, field.getFieldIndex());
    }

    @Override
    public Assigner<PrototypeField> toAssigner(boolean initialize, Expression value) {
        return new FieldAssigner(this, initialize, value);
    }

}