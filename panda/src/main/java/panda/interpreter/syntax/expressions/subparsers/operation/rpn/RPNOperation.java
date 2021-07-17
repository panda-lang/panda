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

package panda.interpreter.syntax.expressions.subparsers.operation.rpn;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.resource.syntax.operator.Operator;

import java.util.Map;
import java.util.Stack;

public final class RPNOperation {

    private final TypeLoader typeLoader;
    private final Map<Operator, RPNOperationSupplier<?>> actions;
    private final Stack<Object> values;

    RPNOperation(TypeLoader typeLoader, Map<Operator, RPNOperationSupplier<?>> actions, Stack<Object> values) {
        this.typeLoader = typeLoader;
        this.actions = actions;
        this.values = values;
    }

    public Expression rectify() {
        return RPNOperationRectifier.getInstance().rectify(typeLoader, actions, values);
    }

    public static RPNOperationBuilder builder() {
        return new RPNOperationBuilder();
    }

}
