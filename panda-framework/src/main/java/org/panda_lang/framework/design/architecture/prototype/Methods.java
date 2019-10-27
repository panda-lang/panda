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

import org.panda_lang.framework.design.architecture.expression.Expression;

import java.util.Collection;
import java.util.Optional;

public interface Methods extends Properties<PrototypeMethod> {

    /**
     * Check if the prototype has method with the given name
     *
     * @param name the name to search for
     * @return true if prototype contains method with the given name
     */
    boolean hasMethodLike(String name);

    /**
     * Get all methods with the given name
     *
     * @param name the name to search for
     * @return collection of methods with the requested name
     */
    Collection<? extends PrototypeMethod> getMethodsLike(String name);

    /**
     * Adjust property to the given arguments
     *
     * @param arguments arguments to match and adjust
     * @return the adjusted executable
     */
    Optional<Adjustment<PrototypeMethod>> getAdjustedArguments(String name, Expression[] arguments);

    /**
     * Get method with the given name and types
     *
     * @param name the name to search for
     * @param parameterTypes the parameter types to search for
     * @return the found method
     */
    Optional<PrototypeMethod> getMethod(String name, Referencable... parameterTypes);

}
