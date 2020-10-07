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

package org.panda_lang.language.interpreter.parser.expression;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;

/**
 * Subparsers is extension parser used by the {@link org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser}
 */
public interface ExpressionSubparser extends Parser, Comparable<ExpressionSubparser> {

    /**
     * Creates worker of the current subparser
     *
     * @return the worker instance
     * @param context the context to use by the worker
     */
    ExpressionSubparserWorker createWorker(Context<?> context);

    /**
     * Get minimal required length of source to use the subparser.
     * Used by {@link org.panda_lang.language.interpreter.parser.expression.ExpressionParser} to improve performance.
     *
     * @return the minimal required length of source
     */
    default int minimalRequiredLengthOfSource() {
        return 1;
    }

    /**
     * Get type of the subparser
     *
     * @return type of subparser
     *
     * @see ExpressionSubparserType
     */
    default ExpressionSubparserType type() {
        return ExpressionSubparserType.MODERATE;
    }

    /**
     * Get category of the subparser
     *
     * @return the category
     *
     * @see org.panda_lang.language.interpreter.parser.expression.ExpressionCategory
     */
    default ExpressionCategory category() {
        return ExpressionCategory.DEFAULT;
    }

    /**
     * Get priority of the subparser. Subparsers are called ascending.
     *
     * @return the priority, by default 1.0
     */
    default double priority() {
        return 1.0;
    }

    @Override
    default int compareTo(@NotNull ExpressionSubparser to) {
        int result = Integer.compare(type().getPriority(), to.type().getPriority());

        if (result != 0) {
            return result;
        }

        return Double.compare(priority(), to.priority());
    }

}
