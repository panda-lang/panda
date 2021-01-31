/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.panda.language.syntax.expressions.subparsers.number;

import org.jetbrains.annotations.Nullable;

public enum NumberType {

    BYTE('B', NumberPriorities.BYTE, PandaNumbers.BYTE, Byte.class),
    SHORT('S', NumberPriorities.SHORT, PandaNumbers.SHORT, Short.class),
    INT('I', NumberPriorities.INT, PandaNumbers.INT, Integer.class),
    LONG('L', NumberPriorities.LONG, PandaNumbers.LONG, Long.class),
    FLOAT('F', NumberPriorities.FLOAT, PandaNumbers.FLOAT, Float.class),
    DOUBLE('D', NumberPriorities.DOUBLE, PandaNumbers.DOUBLE, Double.class);

    private final char letter;
    private final char lowerLetter;
    private final double priority;
    private final String pandaType;
    private final Class<?> javaType;

    NumberType(char letter, double priority, String pandaType, Class<?> javaType) {
        this.letter = letter;
        this.lowerLetter = Character.toLowerCase(this.letter);
        this.priority = priority;
        this.pandaType = pandaType;
        this.javaType = javaType;
    }

    public char getLetter() {
        return letter;
    }

    public String getPandaType() {
        return pandaType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public double getPriority() {
        return priority;
    }

    public static @Nullable NumberType of(Class<?> clazz) {
        for (NumberType type : values()) {
            if (type.getJavaType().equals(clazz)) {
                return type;
            }
        }

        return null;
    }

    public static @Nullable NumberType of(String pandaType) {
        for (NumberType type : values()) {
            if (type.getPandaType().equals(pandaType)) {
                return type;
            }
        }

        return null;
    }

    public static @Nullable NumberType of(char c) {
        for (NumberType type : values()) {
            if (type.letter == c || type.lowerLetter == c) {
                return type;
            }
        }

        return null;
    }

}
