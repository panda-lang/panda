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

public class StringUtils {

    /**
     * Instance of the empty string
     */
    public static final String EMPTY = "";

    /**
     * Check if the specified text is null or empty, throw exception if it is
     *
     * @param text to check
     * @param exceptionMessage the message is used by {@link IllegalArgumentException}
     * @return the checked text
     */
    public static String isEmpty(String text, String exceptionMessage) {
        if (isEmpty(text)) {
            throw new IllegalArgumentException(exceptionMessage);
        }

        return text;
    }

    /**
     * Check if the specified text is null or empty
     *
     * @param text to check
     * @return true if a specified text is null or empty
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Check if the specified string can be parsed as a number
     * Allowed characters:
     *   0-9 - digits
     *   x   - hexadecimal
     *   .   - float
     *
     * @param str string to check
     * @return true if the specified text can be a number
     */
    public static boolean isNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                continue;
            }

            if (c == 'x' || c == '.') {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * Returns the index within this string of the last occurrence of the specified substring
     *
     * @param text    the text to search
     * @param element the substring to search for
     * @param toIndex the previous last index
     * @return the index of the last occurrence of the specified substring, or -1 if there is no such occurrence or toIndex is smaller than 1
     */
    public static int lastIndexOf(String text, String element, int toIndex) {
        if (toIndex < 1) {
            return -1;
        }

        return text.substring(0, toIndex).lastIndexOf(element);
    }

    /**
     * Faster alternative to String#replace
     *
     * @param text the text to search and replace in
     * @param searchString the text to search for
     * @param replacement the text to replace with
     * @return the processed text
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * Pulled from Apache Commons Lang :: StringUtils#replace
     *
     * @author Apache Commons Lang
     */
    private static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString)) {
            return text;
        }

        if (replacement == null) {
            replacement = EMPTY;
        }

        int start = 0;
        int end = text.indexOf(searchString, start);

        if (end == -1) {
            return text;
        }

        int replaceLength = searchString.length();
        int increase = replacement.length() - replaceLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 16;
        StringBuilder sb = new StringBuilder(text.length() + increase);

        while (end != -1) {
            sb.append(text, start, end).append(replacement);
            start = end + replaceLength;

            if (--max == 0) {
                break;
            }

            end = text.indexOf(searchString, start);
        }

        sb.append(text.substring(start));
        return sb.toString();
    }

    /**
     * Replace the first occurrence of the specified pattern in the text
     *
     * @param text the text to search and replace in
     * @param pattern the text to search for
     * @param replacement the text to replace with
     * @return the processed text
     */
    public static String replaceFirst(String text, String pattern, String replacement) {
        return replace(text, pattern, replacement, 1);
    }

    /**
     * Replaces respectively substring of this string that matches the literal target sequence with the specified literal replacement sequence.
     * The replacement proceeds from the beginning of the string to the end, using the next element of the specified values
     *
     * @param text The sequence of char values to be replace replacement – The replacement sequence of char values
     * @param pattern The pattern to search
     * @param values The replacement sequences
     * @return the resulting string
     * @throws java.lang.IllegalArgumentException if the amount of patterns is different than the amount of values
     */
    public static String replaceRespectively(String text, String pattern, String... values) {
        return replaceRespectivelyInternal(text, pattern, false, values);
    }

    /**
     * Replaces respectively substring of this string that matches the literal target sequence with the specified literal replacement sequence.
     * The replacement proceeds from the beginning of the string to the end, using the next element of the specified values
     *
     * @param text The sequence of char values to be replace replacement – The replacement sequence of char values
     * @param pattern The pattern to search
     * @param values The replacement sequences
     * @return the resulting string
     */
    public static String replaceRespectivelyAndSoftly(String text, String pattern, String... values) {
        return replaceRespectivelyInternal(text, pattern, true, values);
    }

    private static String replaceRespectivelyInternal(String text, String pattern, boolean soft, String... values) {
        if (!soft && values.length != countOccurrences(text, pattern)) {
            throw new IllegalArgumentException("The amount of values does not match the amount of pattern occurrences");
        }

        for (String value : values) {
            text = replaceFirst(text, pattern, value);
        }

        return text;
    }

    /**
     * Capitalize characters in string, merged from StringUtils.capitalize [modules - commons-lang:commons-lang3]
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, {@code null} if null String input
     */
    public static String capitalize(String str) {
        int strLen;

        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        int firstCodePoint = str.codePointAt(0);
        int newCodePoint = Character.toTitleCase(firstCodePoint);

        if (firstCodePoint == newCodePoint) {
            return str;
        }

        int newCodePoints[] = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;

        for (int inOffset = Character.charCount(firstCodePoint); inOffset < strLen; ) {
            int codePoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codePoint;
            inOffset += Character.charCount(codePoint);
        }

        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * @return trimmed string
     */
    public static String trimStart(String s) {
        char[] val = s.toCharArray();
        int i = 0;

        while (i < val.length && val[i] <= ' ') {
            i++;
        }

        return s.substring(i, val.length);
    }

    /**
     * @return trimmed string
     */
    public static String trimEnd(String s) {
        int len = s.length();
        char[] val = s.toCharArray();

        while (len > 0 && val[len - 1] <= ' ') {
            len--;
        }

        return s.substring(0, len);
    }

    /**
     * @param str a string to search
     * @return whitespaces at the beginning of the specified string
     */
    public static String extractParagraph(String str) {
        int count = str.indexOf(str.trim());
        return str.substring(0, count);
    }

    /**
     * @param str     a string to search
     * @param findStr a searched string
     * @return amount of occurrences
     */
    public static int countOccurrences(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }

        return count;
    }

    /**
     * @param string     a string to search
     * @param characters searched characters
     * @return true if the specified string contains any of the specified characters
     */
    public static boolean containsCharacter(String string, char... characters) {
        for (char c : string.toCharArray()) {
            for (char character : characters) {
                if (c == character) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param str a string to search
     * @return true if the specified string contains a character other than a letter or a digit
     */
    public static boolean containsSpecialCharacters(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param str a string to search
     * @return true if the string contains a character other than the specified in arrays
     */
    public static boolean containsOtherCharacters(String str, char[]... characters) {
        for (char c : str.toCharArray()) {
            if (CharacterUtils.belongsTo(c, characters)) {
                continue;
            }

            return true;
        }

        return false;
    }

    /**
     * @param str string to convert
     * @return char codes separated by spaces
     */
    @Deprecated
    public static String toCharCodes(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : str.toCharArray()) {
            stringBuilder.append((int) c);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    /**
     * @param spaces number of spaces
     * @return generated indentation
     */
    public static String createIndentation(int spaces) {
        StringBuilder gapBuilder = new StringBuilder();

        for (int i = 0; i < spaces; i++) {
            gapBuilder.append(" ");
        }

        return gapBuilder.toString();
    }

}
