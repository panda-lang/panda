/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.type.member.method;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.Adjustment;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.member.Members;
import org.panda_lang.utilities.commons.function.Option;

/**
 * Container for methods
 */
public interface Methods extends Members<TypeMethod> {

    /**
     * Adjust property to the given arguments
     *
     * @param arguments arguments to match and adjust
     * @return the adjusted executable
     */
    Option<Adjustment<TypeMethod>> getAdjustedArguments(String name, Expression[] arguments);

    /**
     * Get method with the given name and types
     *
     * @param name the name to search for
     * @param parameters the parameter types to search for
     * @return the found method
     */
    Option<TypeMethod> getMethod(String name, Signature[] parameters);

}
