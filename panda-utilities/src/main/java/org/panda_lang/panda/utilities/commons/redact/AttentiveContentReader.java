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

package org.panda_lang.panda.utilities.commons.redact;

import org.panda_lang.panda.utilities.commons.arrays.CharArrayDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AttentiveContentReader {

    private final CharArrayDistributor distributor;
    private char[] openingSequence = BracketContentReader.OPENING_SEQUENCE;
    private char[] closingSequence = BracketContentReader.CLOSING_SEQUENCE;

    public AttentiveContentReader(CharArrayDistributor distributor) {
        this.distributor = distributor;
    }

    public AttentiveContentReader(String pattern) {
        this(new CharArrayDistributor(pattern));
    }

    public List<String> select(char separator) {
        int index = distributor.getIndex();
        List<String> selected = new ArrayList<>();

        StringBuilder content = new StringBuilder();
        Stack<Character> sequences = new Stack<>();

        while (distributor.hasNext()) {
            char current = distributor.next();

            if (current == separator && sequences.size() == 0) {
                selected.add(content.toString());
                content.setLength(0);
                continue;
            }

            BracketContentReader.verifySequences(sequences, openingSequence, closingSequence, current);
            content.append(current);
        }

        if (content.length() > 0) {
            selected.add(content.toString());
        }

        distributor.setIndex(index);
        return selected;
    }

    public void setOpeningSequence(char[] openingSequence) {
        this.openingSequence = openingSequence;
    }

    public void setClosingSequence(char[] closingSequence) {
        this.closingSequence = closingSequence;
    }

}
