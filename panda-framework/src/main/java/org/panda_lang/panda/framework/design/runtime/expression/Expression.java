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

package org.panda_lang.panda.framework.design.runtime.expression;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Frame;

public interface Expression {

    /**
     * Evaluate expression using the specified branch
     *
     * @param frame the frame to use
     * @return nullable value
     */
    Value evaluate(Frame frame);

    /**
     * Check if the expression returns null value
     *
     * @return true if expression returns null
     */
    default boolean isNull() {
        return getReturnType() == null;
    }

    /**
     * Get return type
     *
     * @return the return type
     */
    ClassPrototype getReturnType();

    /**
     * Get expression type
     *
     * @return the type
     */
    ExpressionType getType();

}
