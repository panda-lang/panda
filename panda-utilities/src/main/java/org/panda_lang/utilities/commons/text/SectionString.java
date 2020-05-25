/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.utilities.commons.text;

import org.panda_lang.utilities.commons.CharacterUtils;
import org.panda_lang.utilities.commons.iterable.CharArrayDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class SectionString {

    private final CharArrayDistributor content;
    private final char[] openingCharacters;
    private final char[] closingCharacters;
    private final char[] escapeCharacters;

    private SectionString(SectionStringConfiguration configuration) {
        this.content = configuration.content;
        this.openingCharacters = configuration.openingCharacters;
        this.closingCharacters = configuration.closingCharacters;
        this.escapeCharacters = configuration.escapeCharacters;
    }

    public List<String> split(char separator) {
        int index = content.getIndex();
        List<String> selected = new ArrayList<>();

        StringBuilder content = new StringBuilder();
        Stack<Character> sequences = new Stack<>();

        while (this.content.hasNext()) {
            char current = this.content.next();

            if (current == separator && sequences.size() == 0) {
                selected.add(content.toString());
                content.setLength(0);
                continue;
            }

            SectionStack.verifySequences(sequences, escapeCharacters, openingCharacters, closingCharacters, this.content.getPrevious(), current);
            content.append(current);
        }

        if (content.length() > 0) {
            selected.add(content.toString());
        }

        this.content.setIndex(index);
        return selected;
    }

    public static SectionStringConfiguration of(String content) {
        return new SectionStringConfiguration(content);
    }

    public abstract static class SectionStack {

        protected static final char[] OPENING_SEQUENCE = "({[<\"'".toCharArray();
        protected static final char[] CLOSING_SEQUENCE = ")}]>\"'".toCharArray();

        protected static void verifySequences(Stack<Character> sequences, char[] escapeCharacters, char[] openingCharacters, char[] closingCharacters, char previous, char current) {
            if (CharacterUtils.belongsTo(previous, escapeCharacters)) {
                return;
            }

            if (sequences.size() > 0 && CharacterUtils.belongsTo(current, closingCharacters)) {
                char leftCurrent = openingCharacters[CharacterUtils.getIndex(closingCharacters, current)];

                if (sequences.peek() != leftCurrent) {
                    return;
                }

                sequences.pop();
                return;
            }

            if (CharacterUtils.belongsTo(current, openingCharacters)) {
                sequences.push(current);
            }
        }

    }

    public static final class SectionStringConfiguration {

        private final CharArrayDistributor content;
        private char[] openingCharacters = SectionStack.OPENING_SEQUENCE;
        private char[] closingCharacters = SectionStack.CLOSING_SEQUENCE;
        private char[] escapeCharacters = new char[0];

        SectionStringConfiguration(CharArrayDistributor content) {
            this.content = content;
        }

        SectionStringConfiguration(String content) {
            this(new CharArrayDistributor(content));
        }

        public SectionStringConfiguration withOpeningCharacters(char... openingCharacters) {
            this.openingCharacters = openingCharacters;
            return this;
        }

        public SectionStringConfiguration withClosingCharacters(char... closingCharacters) {
            this.closingCharacters = closingCharacters;
            return this;
        }

        public SectionStringConfiguration withEscapeCharacters(char... escapeCharacters) {
            this.escapeCharacters = escapeCharacters;
            return this;
        }

        public SectionString build() {
            return new SectionString(this);
        }

    }

}
