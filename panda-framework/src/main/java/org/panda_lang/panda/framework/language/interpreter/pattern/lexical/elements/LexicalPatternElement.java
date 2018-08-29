/*
 * Copyright (c) 2016-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements;

import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;

public interface LexicalPatternElement {

    default LexicalPatternWildcard toWildcard() {
        return (LexicalPatternWildcard) this;
    }

    default LexicalPatternNode toNode() {
        return (LexicalPatternNode) this;
    }

    default LexicalPatternUnit toUnit() {
        return (LexicalPatternUnit) this;
    }

    void setOptional(boolean optional);

    void setIdentifier(String identifier);

    void setIsolationType(Isolation isolationType);

    boolean isOptional();

    default boolean isVariant() {
        return this.isNode() && this.toNode().isVariant();
    }

    default boolean isWildcard() {
        return this instanceof LexicalPatternWildcard;
    }

    default boolean isNode() {
        return this instanceof LexicalPatternNode;
    }

    default boolean isUnit() {
        return this instanceof LexicalPatternUnit;
    }

    boolean hasIdentifier();

    Isolation getIsolationType();

    String getIdentifier();

    enum Isolation {

        NONE(false, false),
        START(true, false),
        END(false, true),
        BOTH(true, true);

        private final boolean start, end;

        Isolation(boolean start, boolean end) {
            this.start = start;
            this.end = end;
        }

        public boolean isAny() {
            return this.isStart() || this.isEnd();
        }

        public boolean isStart() {
            return start;
        }

        public boolean isEnd() {
            return end;
        }

        public static Isolation merge(Isolation a, Isolation b) {
            return Isolation.of(a.isStart() || b.isStart(), a.isEnd() || b.isEnd());
        }

        public static Isolation of(String string) {
            return Isolation.of(string.charAt(0), string.charAt(string.length() - 1));
        }

        public static Isolation of(char start, char end) {
            return Isolation.of(CharacterUtils.isWhitespace(start), CharacterUtils.isWhitespace(end));
        }

        public static Isolation of(boolean start, boolean end) {
            for (LexicalPatternUnit.Isolation isolation : values()) {
                if (isolation.start == start && isolation.end == end) {
                    return isolation;
                }
            }

            return Isolation.NONE;
        }

    }

}
