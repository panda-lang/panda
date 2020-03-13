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

package org.panda_lang.framework.language.architecture.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionEvaluator;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.statement.AbstractStatement;

public final class BaseCall extends AbstractStatement implements ExpressionEvaluator {

    private final Expression[] arguments;

    public BaseCall(Location location, Expression[] arguments) {
        super(location);
        this.arguments = arguments;
    }

    @Override
    public Object[] evaluate(ProcessStack stack, @Nullable Object instance) throws Exception {
        return ExpressionUtils.evaluate(stack, instance, arguments);
    }

    public Expression[] getArguments() {
        return arguments;
    }

}
