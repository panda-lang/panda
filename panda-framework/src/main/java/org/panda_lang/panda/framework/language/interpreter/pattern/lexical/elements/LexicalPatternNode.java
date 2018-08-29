/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements;

import java.util.ArrayList;
import java.util.List;

public class LexicalPatternNode extends DefaultLexicalPatternElement {

    private final List<LexicalPatternElement> elements;
    private final boolean variant;

    public LexicalPatternNode(List<LexicalPatternElement> elements, boolean variant) {
        this.elements = elements;
        this.variant = variant;
    }

    public LexicalPatternNode(List<LexicalPatternElement> elements) {
        this(elements, false);
    }

    public LexicalPatternNode(boolean variant) {
        this(new ArrayList<>(), variant);
    }

    public LexicalPatternNode() {
        this(new ArrayList<>());
    }

    public int countUnits() {
        int i = 0;

        for (LexicalPatternElement element : elements) {
            i += element.isUnit() ? 1 : 0;
        }

        return i;
    }

    public List<LexicalPatternUnit> selectUnits(boolean all) {
        List<LexicalPatternUnit> units = new ArrayList<>();

        for (LexicalPatternElement element : elements) {
            if (!element.isUnit()) {
                continue;
            }

            if (!all && element.isOptional()) {
                continue;
            }

            units.add(element.toUnit());
        }

        return units;
    }

    public void add(LexicalPatternElement element) {
        this.elements.add(element);
    }

    public void addAll(List<LexicalPatternElement> elements) {
        this.elements.addAll(elements);
    }

    public boolean isVariant() {
        return variant;
    }

    public List<LexicalPatternElement> getElements() {
        return elements;
    }

}
