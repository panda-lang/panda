/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.design.architecture.dynamic;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;

public interface ScopeInstance extends StandaloneExecutable {

    /**
     * Put value in scope memory
     *
     * @param pointer index of the variable in current scope
     * @param value   new value
     */
    void set(int pointer, @Nullable Value value);

    /**
     * @param pointer index of variable in current scope
     * @return value
     */
    @Nullable Value get(int pointer);

    /**
     * @return array length
     */
    int getAmountOfVariables();

    /**
     * @return the proper scope
     */
    Scope getScope();

}
