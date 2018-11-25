package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard;

public enum WildcardConditionResult {

    NEUTRAL(0, 0),
    ALLOWED(1, 1),
    ILLEGAL(2, -1);

    private final int priority;
    private final int value;

    WildcardConditionResult(int priority, int value) {
        this.priority = priority;
        this.value = value;
    }

    public WildcardConditionResult negate() {
        return of(-1 * value);
    }

    public WildcardConditionResult negate(boolean flag) {
        return flag ? negate() : this;
    }

    public WildcardConditionResult merge(WildcardConditionResult result) {
        return Math.max(priority, result.priority) == priority ? this : result;
    }

    private static WildcardConditionResult of(int value) {
        for (WildcardConditionResult wildcardConditionResult : values()) {
            if (wildcardConditionResult.value == value) {
                return wildcardConditionResult;
            }
        }

        return WildcardConditionResult.NEUTRAL;
    }

}
