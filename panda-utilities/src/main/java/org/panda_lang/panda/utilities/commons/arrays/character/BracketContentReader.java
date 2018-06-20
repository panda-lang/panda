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

package org.panda_lang.panda.utilities.commons.arrays.character;

import org.panda_lang.panda.utilities.commons.arrays.*;
import org.panda_lang.panda.utilities.commons.objects.*;

import java.util.*;

public class BracketContentReader {

    protected static final char[] LEFT_BRACKETS = "({[<".toCharArray();
    protected static final char[] RIGHT_BRACKETS = ")}]>".toCharArray();

    private final CharArrayDistributor distributor;
    private char[] openingBrackets = LEFT_BRACKETS;
    private char[] closingBrackets = RIGHT_BRACKETS;

    public BracketContentReader(CharArrayDistributor distributor) {
        this.distributor = distributor;
    }

    public BracketContentReader(String expression) {
        this(new CharArrayDistributor(expression));
    }

    public String read() {
        StringBuilder content = new StringBuilder();

        List<Character> brackets = new ArrayList<>();
        char leftType = distributor.current();

        if (!CharacterUtils.belongsTo(leftType, openingBrackets)) {
            throw new RuntimeException("Unknown bracket type: " + leftType);
        }

        char rightType = closingBrackets[CharacterUtils.getIndex(openingBrackets, leftType)];

        while (distributor.hasNext()) {
            char current = distributor.next();

            if (current == rightType && brackets.size() == 0) {
                break;
            }

            verifyBrackets(brackets, openingBrackets, closingBrackets, current);
            content.append(current);
        }

        return content.toString();
    }

    protected static void verifyBrackets(List<Character> brackets, char[] openingBrackets, char[] closingBrackets, char current) {
        if (CharacterUtils.belongsTo(current, openingBrackets)) {
            brackets.add(current);
        }
        else if (CharacterUtils.belongsTo(current, closingBrackets)) {
            Character leftCurrent = openingBrackets[CharacterUtils.getIndex(closingBrackets, current)];
            brackets.remove(leftCurrent);
        }
    }

    public void setOpeningBrackets(char[] openingBrackets) {
        this.openingBrackets = openingBrackets;
    }

    public void setClosingBrackets(char[] closingBrackets) {
        this.closingBrackets = closingBrackets;
    }

}
