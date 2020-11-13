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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.runtime.ProcessStack;

public abstract class RPNSimplifiedSupplier<A, B, T> implements RPNOperationSupplier<T> {

    public abstract T get(ProcessStack stack, Object instance, A a, B b);

    public abstract Type returnType(TypeLoader loader);

    @Override
    public RPNOperationAction<T> of(TypeLoader moduleLoader, Expression a, Expression b) {
        return new RPNOperationAction<T>() {
            @Override
            public T get(ProcessStack stack, Object instance) throws Exception {
                return RPNSimplifiedSupplier.this.get(stack, instance, a.evaluate(stack, instance), b.evaluate(stack, instance));
            }

            @Override
            public Signature returnType(TypeLoader loader) {
                return RPNSimplifiedSupplier.this.returnType(loader).getSignature();
            }
        };
    }

    @Override
    public Type returnType(TypeLoader loader, Type a, Type b) {
        return returnType(loader);
    }

}
