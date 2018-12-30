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

package org.panda_lang.panda.framework.design.interpreter.pattern;

public class AbyssPatternData {

    private final String pattern;
    private final String[] wildcards;
    private final int maxNestingLevel;
    private final boolean lastIndexAlgorithm;

    public AbyssPatternData(String pattern, int maxNestingLevel, boolean lastIndexAlgorithm, String... wildcards) {
        this.pattern = pattern;
        this.wildcards = wildcards;
        this.maxNestingLevel = maxNestingLevel;
        this.lastIndexAlgorithm = lastIndexAlgorithm;
    }

    public AbyssPatternData(String pattern, String... wildcards) {
        this(pattern, 0, false, wildcards);
    }

    public boolean getLastIndexAlgorithm() {
        return lastIndexAlgorithm;
    }

    public int getMaxNestingLevel() {
        return maxNestingLevel;
    }

    public String[] getWildcards() {
        return wildcards;
    }

    public String getPattern() {
        return pattern;
    }

}
