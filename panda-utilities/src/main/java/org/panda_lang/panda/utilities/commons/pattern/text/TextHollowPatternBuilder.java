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

import java.util.ArrayList;
import java.util.List;

public class TextHollowPatternBuilder {

    private final List<TextHollowSymbol> hollowSymbols;
    private TextHollowSymbol hollowSymbol;

    protected TextHollowPatternBuilder() {
        this.hollowSymbols = new ArrayList<>();
    }

    public TextHollowPatternBuilder basis(String basis) {
        this.hollowSymbol = new TextHollowSymbol(TextHollowSymbolType.BASIS, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        this.hollowSymbol.addVariant(basis);
        return this;
    }

    public TextHollowPatternBuilder variant(String variant) {
        this.hollowSymbol.addVariant(variant);
        return this;
    }

    public TextHollowPatternBuilder hollow() {
        this.hollowSymbol = new TextHollowSymbol(TextHollowSymbolType.HOLLOW, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        return this;
    }

    public TextHollowPatternBuilder optional(String fragment) {
        this.hollowSymbol = new TextHollowSymbol(TextHollowSymbolType.OPTIONAL, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        this.hollowSymbol.addVariant(fragment);
        return this;
    }

    public TextHollowPatternBuilder optionalSegmentBefore(String segment) {
        this.hollowSymbol.addSegmentBefore(segment);
        return this;
    }

    public TextHollowPatternBuilder optionalSegmentAfter(String segment) {
        this.hollowSymbol.addSegmentAfter(segment);
        return this;
    }

    public TextHollowPatternBuilder compile(String pattern) {
        return compiler().compile(pattern);
    }

    public TextHollowPatternCompiler compiler() {
        return new TextHollowPatternCompiler(this);
    }

    public TextHollowPattern build() {
        return new TextHollowPattern(hollowSymbols);
    }

}
