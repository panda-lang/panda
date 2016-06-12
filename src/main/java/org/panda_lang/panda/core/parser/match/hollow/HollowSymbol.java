package org.panda_lang.panda.core.parser.match.hollow;

import org.panda_lang.panda.core.parser.match.Matcher;

import java.util.ArrayList;
import java.util.Collection;

public class HollowSymbol implements Matcher {

    private final int index;
    private final HollowSymbolType hollowSymbolType;
    private final Collection<String> segmentsBefore;
    private final Collection<String> variants;
    private final Collection<String> segmentsAfter;

    public HollowSymbol(HollowSymbolType hollowSymbolType, int index) {
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
        return hollowSymbolType == HollowSymbolType.OPTIONAL;
    }

    public boolean isHollow() {
        return hollowSymbolType == HollowSymbolType.HOLLOW;
    }

    public boolean isBasis() {
        return hollowSymbolType == HollowSymbolType.BASIS;
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

    public HollowSymbolType getHollowSymbolType() {
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
                return null;
        }
    }

}
