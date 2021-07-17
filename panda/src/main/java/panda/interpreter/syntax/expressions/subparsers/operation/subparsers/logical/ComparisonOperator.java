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

package panda.interpreter.syntax.expressions.subparsers.operation.subparsers.logical;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.number.NumericOperation;

public abstract class ComparisonOperator extends NumericOperation<Boolean> {

    public abstract RPNOperationAction<Boolean> of(int typePriority, Expression a, Expression b);

    @Override
    public RPNOperationAction<Boolean> of(TypeLoader typeLoader, Expression a, Expression b) {
        return of(getHigherPriority(a.getKnownType(), b.getKnownType()), a, b);
    }

    @Override
    public Type returnType(TypeLoader typeLoader, Type a, Type b) {
        return typeLoader.requireType("panda/panda@::Bool");
    }

    @Override
    public Type requiredType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda/panda@::Number");
    }

}
