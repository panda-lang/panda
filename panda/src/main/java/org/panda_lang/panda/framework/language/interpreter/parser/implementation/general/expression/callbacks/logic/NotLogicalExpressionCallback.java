/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.callbacks.logic;

import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;

import java.security.InvalidParameterException;

public class NotLogicalExpressionCallback implements ExpressionCallback {

    private final Expression logicalExpression;

    public NotLogicalExpressionCallback(Expression logicalExpression) {
        if (!logicalExpression.getReturnType().isClassOf("Boolean")) {
            throw new InvalidParameterException("Cannot reverse non logical value");
        }

        this.logicalExpression = logicalExpression;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        Value value = logicalExpression.getExpressionValue(branch);
        boolean val = value.getValue(); // TODO: Handle null?

        return new PandaValue(expression.getReturnType(), !val);
    }

}
