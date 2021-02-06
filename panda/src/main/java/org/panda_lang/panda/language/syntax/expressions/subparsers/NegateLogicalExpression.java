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

package org.panda_lang.panda.language.syntax.expressions.subparsers;

import org.panda_lang.framework.architecture.expression.DynamicExpression;
import org.panda_lang.framework.architecture.expression.Expression;
import org.panda_lang.framework.architecture.type.signature.Signature;
import org.panda_lang.framework.runtime.ProcessStack;

import java.security.InvalidParameterException;

final class NegateLogicalExpression implements DynamicExpression {

    private final Expression logicalExpression;

    public NegateLogicalExpression(Expression logicalExpression) {
        if (!logicalExpression.getKnownType().is("panda/panda@::Bool")) {
            throw new InvalidParameterException("Cannot reverse non logical value");
        }

        this.logicalExpression = logicalExpression;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        Boolean value = logicalExpression.evaluate(stack, instance);
        return !value; // handle npe?
    }

    @Override
    public Signature getReturnType() {
        return logicalExpression.getSignature();
    }

}
