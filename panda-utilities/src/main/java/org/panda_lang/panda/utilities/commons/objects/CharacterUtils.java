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

package org.panda_lang.panda.utilities.commons.objects;

public class CharacterUtils {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static final char[] DIGITS = "0123456789".toCharArray();
    public static final char[] LETTERS = mergeArrays(ALPHABET.toCharArray(), ALPHABET.toUpperCase().toCharArray());
    public static final char[] LITERALS = mergeArrays(LETTERS, DIGITS);

    public static final char TAB = '\t';
    public static final char NO_BREAK_SPACE = '\u00A0';

    /**
     * @param c a character to check
     * @return true if character is whitespace
     */
    public static boolean isWhitespace(char c) {
        return c <= 32 || Character.isWhitespace(c) || c == NO_BREAK_SPACE;
    }

    /**
     * Index of character in the specified array
     *
     * @param characters array of characters
     * @param character  character to check
     * @return index of the specified character or -1 if character is not in the specified array
     */
    public static int getIndex(char[] characters, char character) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == character) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Merge several arrays
     *
     * @param arrays arrays to merge
     * @return array of merged arrays
     */
    public static char[] mergeArrays(char[]... arrays) {
        int size = 0;

        for (char[] array : arrays) {
            size += array.length;
        }

        char[] mergedArray = new char[size];
        int index = 0;

        for (char[] array : arrays) {
            for (char c : array) {
                mergedArray[index++] = c;
            }
        }

        return mergedArray;
    }

    /**
     * @param characters varargs array of characters
     * @return array of characters
     */
    public static char[] arrayOf(char... characters) {
        return characters;
    }

    /**
     * @param character  character to check
     * @param characters array of character arrays
     * @return true if character is in the specified arrays
     */
    public static boolean belongsTo(char character, char[]... characters) {
        for (char[] chars : characters) {
            if (belongsTo(character, chars)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param character  character to check
     * @param characters array of characters
     * @return true if character is in the specified array
     */
    public static boolean belongsTo(char character, char... characters) {
        for (char c : characters) {
            if (c == character) {
                return true;
            }
        }

        return false;
    }

}
