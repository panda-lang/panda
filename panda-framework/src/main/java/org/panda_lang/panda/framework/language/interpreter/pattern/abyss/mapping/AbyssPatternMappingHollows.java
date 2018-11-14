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

package org.panda_lang.panda.framework.language.interpreter.pattern.abyss.mapping;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

import java.util.List;

public class AbyssPatternMappingHollows {

    private final List<Tokens> gaps;

    public AbyssPatternMappingHollows(List<Tokens> gaps) {
        this.gaps = gaps;
    }

    public @Nullable Token getToken(int gapIndex, int tokenIndex) {
        Tokens gap = getGap(gapIndex);

        if (gap == null) {
            return null;
        }

        return gap.getToken(tokenIndex);
    }

    public Tokens getGap(int index) {
        return index < gaps.size() ? gaps.get(index) : null;
    }

    public List<Tokens> getGaps() {
        return gaps;
    }

}
