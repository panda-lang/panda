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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNSimplifiedAction;

public abstract class ComparisonOperatorAction extends RPNSimplifiedAction<Number, Number, Boolean> {

    protected ComparisonOperatorAction(Expression a, Expression b) {
        super(a, b);
    }

    @Override
    public Type returnType(TypeLoader typeLoader) {
        return typeLoader.requireType(boolean.class);
    }

}
