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

package panda.interpreter.architecture.expression;

import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.runtime.ProcessStack;

public final class PandaDynamicExpression extends AbstractDynamicExpression {

    private final ExpressionEvaluator evaluator;

    public PandaDynamicExpression(Signature signature, ExpressionEvaluator evaluator) {
        super(signature);
        this.evaluator = evaluator;
    }

    @Override
    public <T> T evaluate(ProcessStack stack, Object instance) throws Exception {
        return evaluator.evaluate(stack, instance);
    }

}
