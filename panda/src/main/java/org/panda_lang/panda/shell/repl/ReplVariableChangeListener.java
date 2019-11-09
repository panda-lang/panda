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

package org.panda_lang.panda.shell.repl;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.statement.Variable;

/**
 * Listen for changes of variable values
 */
@FunctionalInterface
public interface ReplVariableChangeListener {

    /**
     * Called when value of variable is changed
     *
     * @param variable the affected variable
     * @param previous the previous value
     * @param current a new value
     */
    void onChange(Variable variable, @Nullable Object previous, @Nullable Object current);

}
