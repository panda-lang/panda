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

package org.panda_lang.panda.framework.design.interpreter.pattern.progressive;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.pattern.MatcherResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

import java.util.ArrayList;
import java.util.List;

public class ProgressivePatternResult implements MatcherResult {

    private final Snippet source;
    private final List<ProgressivePatternElement> elements;
    private boolean matched;

    protected ProgressivePatternResult(Snippet source) {
        this.elements = new ArrayList<>(3);
        this.source = source;
    }

    protected void succeed() {
        this.matched = true;
    }

    protected void addElement(ProgressivePatternElement element) {
        this.elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public boolean isMatched() {
        return matched;
    }

    public @Nullable String get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }

        return elements.get(index).toString();
    }

    public List<ProgressivePatternElement> getElements() {
        return elements;
    }

    @Override
    public Snippet getSource() {
        return source;
    }

}
