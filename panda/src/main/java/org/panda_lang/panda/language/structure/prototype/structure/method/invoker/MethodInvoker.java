/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.method.invoker;

import org.panda_lang.panda.core.structure.dynamic.StandaloneExecutable;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.ExpressionUtils;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;

public class MethodInvoker implements StandaloneExecutable {

    private final Method method;
    private final Expression expression;
    private final Expression[] arguments;

    public MethodInvoker(Method method, Expression instance, Expression[] arguments) {
        this.method = method;
        this.expression = instance;
        this.arguments = arguments;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        Object instance = null;
        Value[] values = ExpressionUtils.getValues(branch, arguments);

        if (expression != null) {
            Value value = expression.getExpressionValue(branch);
            instance = value.getValue();
        }

        method.invoke(branch, instance, values);
    }

}
