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
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.syntax.expressions.subparsers.operation.rpn.RPNOperationAction;
import panda.interpreter.syntax.expressions.subparsers.operation.rpn.RPNOperationSupplier;

public class OrOperation implements RPNOperationSupplier<Boolean> {

    @Override
    public RPNOperationAction<Boolean> of(TypeLoader typeLoader, Expression a, Expression b) {
        return new RPNOperationAction<Boolean>() {
            @Override
            public Boolean get(ProcessStack stack, Object instance) throws Exception {
                Boolean aValue = a.evaluate(stack, instance);

                if (aValue) {
                    return true;
                }

                return b.evaluate(stack, instance);
            }

            @Override
            public Signature returnType(TypeLoader typeLoader) {
                return requiredType(typeLoader).getSignature();
            }
        };
    }

    @Override
    public Type returnType(TypeLoader typeLoader, Type a, Type b) {
        return requiredType(typeLoader);
    }

    @Override
    public Type requiredType(TypeLoader typeLoader) {
        return typeLoader.requireType("panda/panda@::Bool");
    }

}
