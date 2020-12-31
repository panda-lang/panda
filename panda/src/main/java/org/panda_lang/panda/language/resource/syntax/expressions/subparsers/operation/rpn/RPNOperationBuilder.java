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

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;

import java.util.Map;

public final class RPNOperationBuilder {

    private Context<?> context;
    private Operation operation;
    protected Map<Operator, Integer> priorities;
    protected Map<Operator, RPNOperationSupplier<?>> suppliers;

    public RPNOperationBuilder withOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public RPNOperationBuilder withData(Context<?> context) {
        this.context = context;
        return this;
    }

    public RPNOperationBuilder withPriorities(Map<Operator, Integer> priorities) {
        this.priorities = priorities;
        return this;
    }

    public RPNOperationBuilder withActions(Map<Operator, RPNOperationSupplier<?>> actions) {
        this.suppliers = actions;
        return this;
    }

    public RPNOperation build() {
        return new RPNOperationTransformer(this).parse(context, operation);
    }

}
