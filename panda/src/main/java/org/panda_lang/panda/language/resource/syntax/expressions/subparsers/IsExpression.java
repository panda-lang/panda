/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.runtime.ProcessStack;
import org.panda_lang.language.architecture.expression.DynamicExpression;

final class IsExpression implements DynamicExpression {

    private final Type returnType;
    private final Expression value;
    private final Signature requestedSignature;

    IsExpression(Type returnType, Expression value, Signature requestedSignature) {
        this.returnType = returnType;
        this.value = value;
        this.requestedSignature = requestedSignature;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        return requestedSignature.getPrimaryType().getAssociated().get().isAssignableFrom(value.evaluate(stack, instance).getClass());
    }

    @Override
    public Signature getReturnType() {
        return returnType.getSignature();
    }

}
