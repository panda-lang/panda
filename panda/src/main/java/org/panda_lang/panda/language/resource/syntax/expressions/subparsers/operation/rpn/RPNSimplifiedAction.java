/*
 * Copyright (c) 2015-2020 Dzikoysk
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

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.ProcessStack;

public abstract class RPNSimplifiedAction<A, B, R> implements RPNOperationAction<R> {

    private final Expression a;
    private final Expression b;

    protected RPNSimplifiedAction(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public abstract R get(ProcessStack stack, Object instance, A a, B b);

    @Override
    public R get(ProcessStack stack, Object instance) throws Exception {
        return get(stack, instance, a.evaluate(stack, instance), b.evaluate(stack, instance));
    }

    @Override
    public Prototype returnType(ModuleLoader loader) {
        return null;
    }

}
