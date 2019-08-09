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

package org.panda_lang.panda.framework.design.interpreter.parser.expression;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;

/**
 * Subparsers is extension parser used by the {@link org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser}
 */
public interface ExpressionSubparser extends Parser, Comparable<ExpressionSubparser> {

    /**
     * Creates worker of the current subparser
     *
     * @return the worker instance
     */
    ExpressionSubparserWorker createWorker();

    @Override
    default int compareTo(@NotNull ExpressionSubparser to) {
        int result = Integer.compare(getSubparserType().getPriority(), to.getSubparserType().getPriority());

        if (result != 0) {
            return result;
        }

        return Double.compare(getPriority(), to.getPriority());
    }

    /**
     * Get minimal required length of source to use the subparser.
     * Used by {@link org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser} to improve performance.
     *
     * @return the minimal required length of source
     */
    default int getMinimalRequiredLengthOfSource() {
        return 1;
    }

    /**
     * Get type of the subparser
     *
     * @return type of subparser
     *
     * @see ExpressionSubparserType
     */
    default ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.MODERATE;
    }

    /**
     * Get category of the subparser
     *
     * @return the category
     *
     * @see org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionCategory
     */
    default ExpressionCategory getCategory() {
        return ExpressionCategory.DEFAULT;
    }

    /**
     * Get priority of the subparser. Subparsers are called ascending.
     *
     * @return the priority, by default 1.0
     */
    default double getPriority() {
        return 1.0;
    }

    /**
     * Get name of the subparser
     *
     * @return the name
     */
    String getSubparserName();

}
