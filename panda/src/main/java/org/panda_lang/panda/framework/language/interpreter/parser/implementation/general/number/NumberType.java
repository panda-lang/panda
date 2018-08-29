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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number;

import org.jetbrains.annotations.Nullable;

public enum NumberType {

    BYTE('B'),
    SHORT('S'),
    INT('I'),
    LONG('L'),
    FLOAT('F'),
    DOUBLE('D');

    private final char letter;

    NumberType(char c) {
        this.letter = c;
    }

    public char getLetter() {
        return letter;
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
