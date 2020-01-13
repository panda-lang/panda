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

package org.panda_lang.framework.language.architecture.expression;

import org.panda_lang.framework.design.architecture.expression.ExpressionEvaluator;
import org.panda_lang.framework.design.architecture.prototype.Referencable;
import org.panda_lang.framework.design.runtime.ProcessStack;

public final class PandaDynamicExpression extends AbstractDynamicExpression {

    private final ExpressionEvaluator evaluator;

    public PandaDynamicExpression(Referencable referencable, ExpressionEvaluator evaluator) {
        super(referencable.toReference());
        this.evaluator = evaluator;
    }

    @Override
    public <T> T evaluate(ProcessStack stack, Object instance) throws Exception {
        return evaluator.evaluate(stack, instance);
    }

}
