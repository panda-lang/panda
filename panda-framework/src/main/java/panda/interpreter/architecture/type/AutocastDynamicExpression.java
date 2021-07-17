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

package panda.interpreter.architecture.type;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.runtime.ProcessStack;
import panda.interpreter.architecture.expression.DynamicExpression;

public final class AutocastDynamicExpression implements DynamicExpression {

    private final Expression original;
    private final Signature to;
    private final Autocast<?, ?> autocast;

    public AutocastDynamicExpression(Expression original, Signature to, Autocast<?, ?> autocast) {
        this.original = original;
        this.to = to;
        this.autocast = autocast;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        return autocast.cast(original.getSignature(), original.evaluate(stack, instance), to);
    }

    @Override
    public Signature getReturnType() {
        return to;
    }

}
