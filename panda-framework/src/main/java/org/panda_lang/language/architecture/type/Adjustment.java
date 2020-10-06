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

package org.panda_lang.language.architecture.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.type.member.Member;

/**
 * Container for arguments adjusted to the property signature
 *
 * @param <T> generic type of property
 */
public class Adjustment<T extends Member> {

    private final T executable;
    private final Expression[] arguments;

    public Adjustment(T executable, @Nullable Expression[] arguments) {
        this.executable = executable;
        this.arguments = arguments;
    }

    /**
     * Get adjusted arguments
     *
     * @return the array of arguments
     */
    public Expression[] getArguments() {
        return arguments;

    }

    /**
     * Get associated parametrized executable
     *
     * @return the executable
     */
    public T getExecutable() {
        return executable;
    }

}
