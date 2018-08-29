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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.arrays.CharArrayDistributor;
import org.panda_lang.panda.utilities.commons.objects.CharacterUtils;

import java.util.Stack;

public class BracketContentReader {

    protected static final char[] OPENING_SEQUENCE = "({[<\"".toCharArray();
    protected static final char[] CLOSING_SEQUENCE = ")}]>\"".toCharArray();

    private final CharArrayDistributor distributor;
    private char[] openingSequence = OPENING_SEQUENCE;
    private char[] closingSequence = CLOSING_SEQUENCE;

    public BracketContentReader(CharArrayDistributor distributor) {
        this.distributor = distributor;
    }

    public BracketContentReader(String expression) {
        this(new CharArrayDistributor(expression));
    }

    public @Nullable String readCurrent() {
        distributor.previous();
        return read();
    }

    public @Nullable String read() {
        Stack<Character> sequences = new Stack<>();
        char leftType = distributor.next();

        if (!CharacterUtils.belongsTo(leftType, openingSequence)) {
            throw new RuntimeException("Unknown bracket type: " + leftType);
        }

        StringBuilder content = new StringBuilder();
        char rightType = closingSequence[CharacterUtils.getIndex(openingSequence, leftType)];

        while (distributor.hasNext()) {
            char current = distributor.next();

            if (current == rightType && sequences.size() == 0) {
                break;
            }

            verifySequences(sequences, openingSequence, closingSequence, current);
            content.append(current);
        }

        if (sequences.size() > 0) {
            return null;
        }

        return content.toString();
    }

    public void setOpeningSequence(char[] openingSequence) {
        this.openingSequence = openingSequence;
    }

    public void setClosingSequence(char[] closingSequence) {
        this.closingSequence = closingSequence;
    }

    protected static void verifySequences(Stack<Character> sequences, char[] openingSequence, char[] closingSequence, char current) {
        if (sequences.size() > 0 && CharacterUtils.belongsTo(current, closingSequence)) {
            Character leftCurrent = openingSequence[CharacterUtils.getIndex(closingSequence, current)];

            if (sequences.peek() != leftCurrent) {
                return;
            }

            sequences.pop();
            return;
        }

        if (CharacterUtils.belongsTo(current, openingSequence)) {
            sequences.push(current);
        }
    }

}
