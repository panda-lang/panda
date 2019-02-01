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

package org.panda_lang.panda.framework.language.resource.parsers.general.number;

import org.jetbrains.annotations.Nullable;

public enum NumberType {

    BYTE('B', NumberPriorities.BYTE),
    SHORT('S', NumberPriorities.SHORT),
    INT('I', NumberPriorities.INT),
    LONG('L', NumberPriorities.LONG),
    FLOAT('F', NumberPriorities.FLOAT),
    DOUBLE('D', NumberPriorities.DOUBLE);

    private final char letter;
    private final double priority;

    NumberType(char c, double priority) {
        this.letter = c;
        this.priority = priority;
    }

    public char getLetter() {
        return letter;
    }

    public double getPriority() {
        return priority;
    }

    public static @Nullable NumberType of(char c) {
        for (NumberType type : values()) {
            if (type.getLetter() == c || type.getLetter() == Character.toUpperCase(c)) {
                return type;
            }
        }

        return null;
    }

}
