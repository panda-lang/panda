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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

public class ExpressionUtils {

    public static Value[] getValues(ExecutableBranch branch, Expression... expressions) {
        Value[] values = new Value[expressions.length];

        for (int i = 0; i < values.length; i++) {
            Expression expression = expressions[i];
            values[i] = expression.getExpressionValue(branch);
        }

        return values;
    }

    public static ClassPrototype[] toTypes(Expression... expressions) {
        ClassPrototype[] prototypes = new ClassPrototype[expressions.length];

        for (int i = 0; i < prototypes.length; i++) {
            Expression expression = expressions[i];
            prototypes[i] = expression.getReturnType();
        }

        return prototypes;
    }

}
