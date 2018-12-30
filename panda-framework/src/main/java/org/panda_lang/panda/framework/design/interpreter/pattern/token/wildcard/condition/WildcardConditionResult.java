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

package org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.condition;

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
