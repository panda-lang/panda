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

import panda.interpreter.architecture.type.Type;
import panda.interpreter.architecture.type.Typed;
import panda.interpreter.architecture.type.signature.Signed;

/**
 * Represents dynamic values
 */
public interface Expression extends Typed, Signed, Expressible, ExpressionEvaluator {

    /**
     * Get expression type
     *
     * @return the type
     */
    ExpressionValueType getExpressionType();

    /**
     * Check if the expression returns null value
     *
     * @return true if expression returns null
     */
    default boolean isNull() {
        return getSignature() == null;
    }

    @Override
    default Expression toExpression() {
        return this;
    }

    @Override
    default Type toType() {
        return getKnownType();
    }

}
