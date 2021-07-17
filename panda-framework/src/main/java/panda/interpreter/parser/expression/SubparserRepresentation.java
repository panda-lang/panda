/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.parser.expression;

import org.jetbrains.annotations.NotNull;

public final class SubparserRepresentation implements Comparable<SubparserRepresentation> {

    private final ExpressionSubparser subparser;
    private int usages;

    public SubparserRepresentation(ExpressionSubparser subparser) {
        this.subparser = subparser;
    }

    @Override
    public int compareTo(@NotNull SubparserRepresentation to) {
        int byPriority = subparser.compareTo(to.getSubparser());
        return byPriority == 0 ? subparser.name().compareTo(to.subparser.name()) : byPriority;
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
