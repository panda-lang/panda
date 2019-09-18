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

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.runtime.expression.DynamicExpression;

final class IsExpression implements DynamicExpression {

    private final Expression value;
    private final ClassPrototypeReference requestedTypeReference;

    IsExpression(Expression value, ClassPrototypeReference requestedTypeReference) {
        this.value = value;
        this.requestedTypeReference = requestedTypeReference;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) {
        return requestedTypeReference.getAssociatedClass().isAssignableFrom(value.evaluate(stack, instance).getClass());
    }

    @Override
    public ClassPrototype getReturnType() {
        return PandaTypes.BOOLEAN;
    }

}
