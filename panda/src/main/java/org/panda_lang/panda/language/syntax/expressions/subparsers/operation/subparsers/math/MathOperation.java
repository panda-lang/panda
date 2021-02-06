/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.expressions.subparsers.operation.subparsers.math;

import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import org.panda_lang.panda.language.syntax.expressions.subparsers.operation.subparsers.number.NumericOperation;

public abstract class MathOperation extends NumericOperation<Number> {

    public abstract RPNOperationAction<Number> of(Type returnType, int priority, Expression a, Expression b);

    @Override
    public RPNOperationAction<Number> of(TypeLoader typeLoader, Expression a, Expression b) {
        Type returnType = returnType(typeLoader, a.getKnownType(), b.getKnownType());
        return of(returnType, getPriority(returnType), a, b);
    }

    @Override
    public Type returnType(TypeLoader typeLoader, Type a, Type b) {
        return estimateType(a, b);
    }

    @Override
    public Type requiredType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda@::Number");
    }

}
