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

package org.panda_lang.panda.utilities.commons.pattern.text;

import org.panda_lang.panda.utilities.commons.pattern.PatternMatcher;

import java.util.ArrayList;
import java.util.Collection;

public class TextHollowSymbol implements PatternMatcher {

    private final int index;
    private final TextHollowSymbolType hollowSymbolType;
    private final Collection<String> segmentsBefore;
    private final Collection<String> variants;
    private final Collection<String> segmentsAfter;

    public TextHollowSymbol(TextHollowSymbolType hollowSymbolType, int index) {
        this.index = index;
        this.hollowSymbolType = hollowSymbolType;
        this.segmentsBefore = new ArrayList<>(1);
        this.variants = new ArrayList<>(1);
        this.segmentsAfter = new ArrayList<>(1);
    }

    @Override
    public boolean match(String expression) {
        for (String variant : variants) {
            if (variant.equals(expression)) {
                return true;
            }
        }
        return false;
    }

    public void addSegmentBefore(String segment) {
        this.segmentsBefore.add(segment);
    }

    public void addVariant(String variant) {
        this.variants.add(variant);
    }

    public void addSegmentAfter(String segment) {
        this.segmentsAfter.add(segment);
    }

    public boolean isOptional() {
        return hollowSymbolType == TextHollowSymbolType.OPTIONAL;
    }

    public boolean isHollow() {
        return hollowSymbolType == TextHollowSymbolType.HOLLOW;
    }

    public boolean isBasis() {
        return hollowSymbolType == TextHollowSymbolType.BASIS;
    }

    public Collection<String> getSegmentsBefore() {
        return segmentsBefore;
    }

    public Collection<String> getSegmentsAfter() {
        return segmentsAfter;
    }

    public Collection<String> getVariants() {
        return variants;
    }

    public TextHollowSymbolType getHollowSymbolType() {
        return hollowSymbolType;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        switch (getHollowSymbolType()) {
            case BASIS:
                return variants.toString();
            case HOLLOW:
                return "*";
            case OPTIONAL:
                return segmentsBefore + "[ ]" + segmentsAfter;
            default:
                return "<unknown type>";
        }
    }

}
