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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import org.jetbrains.annotations.NotNull;

public interface ExpressionSubparser extends Comparable<ExpressionSubparser> {

    ExpressionSubparserWorker createWorker();

    String getSubparserName();

    @Override
    default int compareTo(@NotNull ExpressionSubparser to) {
        int result = Integer.compare(getSubparserType().getPriority(), to.getSubparserType().getPriority());

        if (result != 0) {
            return result;
        }

        return Double.compare(getPriority(), to.getPriority());
    }

    default ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.MODERATE;
    }

    default ExpressionType getType() {
        return ExpressionType.SINGULAR;
    }

    default double getPriority() {
        return 1.0;
    }

}
