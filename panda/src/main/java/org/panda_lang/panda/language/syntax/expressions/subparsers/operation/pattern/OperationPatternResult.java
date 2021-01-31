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

package org.panda_lang.panda.language.syntax.expressions.subparsers.operation.pattern;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.interpreter.token.Snippet;

import java.util.ArrayList;
import java.util.List;

public final class OperationPatternResult {

    private final Snippet source;
    private final List<OperationPatternElement> elements;
    private boolean matched;

    protected OperationPatternResult(Snippet source) {
        this.elements = new ArrayList<>(3);
        this.source = source;
    }

    protected void succeed() {
        this.matched = true;
    }

    protected void addElement(OperationPatternElement element) {
        this.elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    public boolean isMatched() {
        return matched;
    }

    public @Nullable String get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }

        return elements.get(index).toString();
    }

    public List<OperationPatternElement> getElements() {
        return elements;
    }

    public Snippet toSnippet() {
        return source;
    }

}
