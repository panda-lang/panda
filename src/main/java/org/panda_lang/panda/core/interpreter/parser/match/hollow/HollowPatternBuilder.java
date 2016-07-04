package org.panda_lang.panda.core.interpreter.parser.match.hollow;

import java.util.ArrayList;
import java.util.List;

public class HollowPatternBuilder {

    private final List<HollowSymbol> hollowSymbols;
    private HollowSymbol hollowSymbol;

    protected HollowPatternBuilder() {
        this.hollowSymbols = new ArrayList<>();
    }

    public HollowPatternBuilder basis(String basis) {
        this.hollowSymbol = new HollowSymbol(HollowSymbolType.BASIS, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        this.hollowSymbol.addVariant(basis);
        return this;
    }

    public HollowPatternBuilder variant(String variant) {
        this.hollowSymbol.addVariant(variant);
        return this;
    }

    public HollowPatternBuilder hollow() {
        this.hollowSymbol = new HollowSymbol(HollowSymbolType.HOLLOW, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        return this;
    }

    public HollowPatternBuilder optional(String fragment) {
        this.hollowSymbol = new HollowSymbol(HollowSymbolType.OPTIONAL, hollowSymbols.size());
        this.hollowSymbols.add(hollowSymbol);
        this.hollowSymbol.addVariant(fragment);
        return this;
    }

    public HollowPatternBuilder optionalSegmentBefore(String segment) {
        this.hollowSymbol.addSegmentBefore(segment);
        return this;
    }

    public HollowPatternBuilder optionalSegmentAfter(String segment) {
        this.hollowSymbol.addSegmentAfter(segment);
        return this;
    }

    public HollowPatternBuilder compile(String pattern) {
        return compiler().compile(pattern);
    }

    public HollowPatternCompiler compiler() {
        return new HollowPatternCompiler(this);
    }

    public HollowPattern build() {
        return new HollowPattern(hollowSymbols);
    }

}
