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

package org.panda_lang.framework.language.interpreter.parser.expression;

import org.jetbrains.annotations.NotNull;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;

public final class PandaExpressionSubparserRepresentation implements Comparable<PandaExpressionSubparserRepresentation> {

    private final ExpressionSubparser subparser;
    private int usages;

    public PandaExpressionSubparserRepresentation(ExpressionSubparser subparser) {
        this.subparser = subparser;
    }

    @Override
    public int compareTo(@NotNull PandaExpressionSubparserRepresentation to) {
        int result = subparser.compareTo(to.getSubparser());
        return result == 0 ? Integer.compare(usages, to.getUsages()) : result;
    }

    public void increaseUsages() {
        usages++;
    }

    public int getUsages() {
        return usages;
    }

    public ExpressionSubparser getSubparser() {
        return subparser;
    }

}
