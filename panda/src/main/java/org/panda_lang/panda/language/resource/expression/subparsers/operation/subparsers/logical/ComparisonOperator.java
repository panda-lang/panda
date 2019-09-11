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

package org.panda_lang.panda.language.resource.expression.subparsers.operation.subparsers.logical;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.resource.expression.subparsers.operation.rpn.RPNOperationAction;
import org.panda_lang.panda.language.resource.expression.subparsers.operation.subparsers.number.NumberOperation;

public abstract class ComparisonOperator extends NumberOperation {

    public abstract RPNOperationAction of(int compared);

    @Override
    public RPNOperationAction of(Expression a, Expression b) {
        ClassPrototype comparedType = estimateType(a.getReturnType(), b.getReturnType());
        return of(super.getPriority(comparedType));
    }

    @Override
    public ClassPrototype returnType(ClassPrototype a, ClassPrototype b) {
        return PandaTypes.BOOLEAN;
    }

    @Override
    public ClassPrototype requiredType() {
        return PandaTypes.NUMBER;
    }

}
