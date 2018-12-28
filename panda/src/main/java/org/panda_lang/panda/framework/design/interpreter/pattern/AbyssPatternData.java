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
