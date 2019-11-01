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

package org.panda_lang.framework.design.architecture.prototype;

import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.architecture.expression.Expression;

/**
 * Represents field property
 */
public interface PrototypeField extends ExecutableProperty, Variable {

    /**
     * Set the default value of this field,
     * assigned when a new instance of the prototype is created
     *
     * @param defaultValue the default value to use
     */
    void setDefaultValue(Expression defaultValue);

    /**
     * Set static value
     *
     * @param staticValue the static value
     */
    void setStaticValue(Object staticValue);

    /**
     * Check if field is static
     *
     * @return true if static, otherwise false
     */
    boolean isStatic();

    /**
     * Check if field comes from Java mappings.
     * It may be important data for e.g. setting value.
     *
     * @return true if field comes from java
     */
    boolean isNative();

    /**
     * Check if the field has default value
     *
     * @return true if field has default value
     */
    boolean hasDefaultValue();

    /**
     * Get static value
     *
     * @param <T> type of value
     * @return the static value
     */
    <T> T getStaticValue();

    /**
     * Get default value
     *
     * @return expression that returns default value
     */
    Expression getDefaultValue();

}
