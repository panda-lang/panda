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

package org.panda_lang.panda.utilities.commons;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.text.ContentJoiner;

import java.util.function.Function;
import java.util.function.Predicate;

public class StringUtils {

    /**
     * Instance of the empty string
     */
    public static final String EMPTY = "";

    /**
     * Instance of the empty array of string
     */
    public static final String[] EMPTY_ARRAY = new String[0];

    /**
     * Split text by the specified delimiter, but only once (to the first occurrence of the specified delimiter)
     *
     * @param text      the text to split
     * @param delimiter the delimiter to search for
     * @return array of splitted text
     */
    public static String[] splitFirst(String text, String delimiter) {
        int index = text.indexOf(delimiter);

        if (index == -1) {
            return EMPTY_ARRAY;
        }

        return new String[] { text.substring(0, index), text.substring(index + 1) };
    }

    /**
     * Split text by the specified delimiter (without regex)
     *
     * @param text      the text to split
     * @param delimiter the delimiting text
     * @return the array of elements
     */
    public static String[] split(String text, String delimiter) {
        int occurrences = countOccurrences(text, delimiter);

        if (occurrences == 0) {
            return new String[] { text };
        }

        String[] elements = new String[occurrences + 1];
        int arrayIndex = 0;
        int index = 0;

        while (index < text.length()) {
            int currentIndex = text.indexOf(delimiter, index);

            if (currentIndex == -1) {
                elements[arrayIndex++] = text.substring(index);
                break;
            }

            elements[arrayIndex++] = text.substring(index, currentIndex);
            index = currentIndex + delimiter.length();
        }

        if (text.endsWith(delimiter)) {
            elements[arrayIndex] = EMPTY;
        }

        return elements;
    }

    /**
     * Last index of the element with given amount of last occurrences to ignore
     *
     * @param text        the text to search in
     * @param element     the element to search for
     * @param occurrences amount of occurrences to ignore before the element to search for
     * @return index of the element, -1 when the element is not found
     */
    public static int lastIndexOfBefore(String text, String element, int occurrences) {
        int occurrence = 0;
        int index = text.length() - 1;

        while (index > -1) {
            int currentIndex = lastIndexOf(text, element, index);

            if (currentIndex == -1) {
                break;
            }

            occurrence++;

            if (occurrence == occurrences) {
                return currentIndex;
            }

            index = currentIndex + text.length();
        }

        return -1;
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
        if (toIndex < 1 || toIndex > text.length()) {
            return -1;
        }

        return text.substring(0, toIndex).lastIndexOf(element);
    }

    /**
     * Faster alternative to String#replace
     *
     * @param text         the text to search and replace in
     * @param searchString the text to search for
     * @param replacement  the text to replace with
     * @return the resulting text
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 0, -1);
    }

    /**
     * Replaces each substring of the string that equals
     * to the literal target sequence with the specified literal replacement sequence
     * <p>
     * ~ Based on Apache Commons Lang :: StringUtils#replace
     *
     * @param text        the text to search and replace in
     * @param pattern     the text to search for
     * @param replacement the text to replace with
     * @param fromIndex   the index from which to start the search
     * @param max         amount of occurrences to replace
     * @return the resulting string
     */
    private static String replace(@Nullable String text, @Nullable String pattern, String replacement, int fromIndex, int max) {
        if (isEmpty(text) || isEmpty(pattern)) {
            return text;
        }

        int start = 0;
        int end = text.indexOf(pattern, fromIndex);

        if (end == -1) {
            return text;
        }

        int replaceLength = pattern.length();
        int increase = replacement.length() - replaceLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 16;
        StringBuilder builder = new StringBuilder(text.length() + increase);

        while (end != -1) {
            builder.append(text, start, end).append(replacement);
            start = end + replaceLength;

            if (--max == 0) {
                break;
            }

            end = text.indexOf(pattern, start);
        }

        builder.append(text.substring(start));
        return builder.toString();
    }

    /**
     * Replace the first occurrence of the specified pattern in the text
     *
     * @param text        the text to search and replace in
     * @param pattern     the text to search for
     * @param replacement the text to replace with
     * @return the processed text
     */
    public static String replaceFirst(String text, String pattern, String replacement) {
        return replaceFirst(text, pattern, replacement, 0);
    }

    /**
     * Replace the first occurrence of the specified pattern in the text
     *
     * @param text        the text to search and replace in
     * @param pattern     the text to search for
     * @param replacement the text to replace with
     * @param start       position to start searching for
     * @return the processed text
     */
    public static String replaceFirst(String text, String pattern, String replacement, int start) {
        return replace(text, pattern, replacement, start, 1);
    }

    /**
     * Replaces respectively substring of this string that matches the literal target sequence with the specified literal replacement sequence.
     * The replacement proceeds from the beginning of the string to the end, using the next element of the specified values
     *
     * @param text    The sequence of char values to be replace replacement – The replacement sequence of char values
     * @param pattern The pattern to search
     * @param values  The replacement sequences
     * @return the resulting string
     *
     * @throws java.lang.IllegalArgumentException if the amount of patterns is different than the amount of values
     */
    public static String replaceRespectively(String text, String pattern, String... values) {
        return replaceRespectivelyInternal(text, pattern, false, values);
    }

    /**
     * Replaces respectively substring of this string that matches the literal target sequence with the specified literal replacement sequence.
     * The replacement proceeds from the beginning of the string to the end, using the next element of the specified values
     *
     * @param text    The sequence of char values to be replace replacement – The replacement sequence of char values
     * @param pattern The pattern to search
     * @param values  The replacement sequences
     * @return the resulting string
     */
    public static String replaceRespectivelyAndSoftly(String text, String pattern, String... values) {
        return replaceRespectivelyInternal(text, pattern, true, values);
    }

    private static String replaceRespectivelyInternal(String text, String pattern, boolean soft, String... values) {
        if (!soft && values.length != countOccurrences(text, pattern)) {
            throw new IllegalArgumentException("The amount of values does not match the amount of pattern occurrences");
        }

        if (ArrayUtils.containsNull(values)) {
            throw new IllegalArgumentException("Array of values contains null");
        }

        int[] positions = new int[values.length];
        int previousIndex = 0;

        for (int i = 0; i < positions.length; i++) {
            int index = text.indexOf(pattern, previousIndex);
            positions[i] = index;
            previousIndex = index + pattern.length();
        }

        int diff = 0;

        for (int i = 0; i < values.length; i++) {
            text = replaceFirst(text, pattern, values[i], positions[i] + diff);
            diff += -pattern.length() + values[i].length();
        }

        return text;
    }

    /**
     * Capitalize characters in string, merged from StringUtils.capitalize [modules - commons-lang:commons-lang3]
     *
     * @param text the string to capitalize, may be null
     * @return the capitalized string, {@code null} if null string input
     */
    public static String capitalize(String text) {
        int strLen;

        if (text == null || (strLen = text.length()) == 0) {
            return text;
        }

        int firstCodePoint = text.codePointAt(0);
        int newCodePoint = Character.toTitleCase(firstCodePoint);

        if (firstCodePoint == newCodePoint) {
            return text;
        }

        int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;

        for (int inOffset = Character.charCount(firstCodePoint); inOffset < strLen; ) {
            int codePoint = text.codePointAt(inOffset);
            newCodePoints[outOffset++] = codePoint;
            inOffset += Character.charCount(codePoint);
        }

        return new String(newCodePoints, 0, outOffset);
    }

    /**
     * Check how the specified text starts
     *
     * @param text   the text to check
     * @param filter condition
     * @return result of the condition
     */
    public static boolean startsWith(String text, Predicate<char[]> filter) {
        if (text.length() == 0) {
            return false;
        }

        return filter.test(text.toCharArray());
    }

    /**
     * Trim the beginning of the text
     *
     * @param text the text to trim
     * @return trimmed string
     */
    public static String trimStart(String text) {
        char[] chars = text.toCharArray();
        int index = 0;

        while (index < chars.length && chars[index] <= ' ') {
            index++;
        }

        return text.substring(index, chars.length);
    }

    /**
     * Trim the of end of the specified text
     *
     * @param text the text to trim
     * @return trimmed string
     */
    public static String trimEnd(String text) {
        int length = text.length();
        char[] chars = text.toCharArray();

        while (length > 0 && chars[length - 1] <= ' ') {
            length--;
        }

        return text.substring(0, length);
    }

    /**
     * Extracts paragraph/indentation from the beginning of the text
     *
     * @param str a string to search
     * @return whitespaces at the beginning of the specified string
     */
    public static String extractParagraph(String str) {
        int count = str.indexOf(str.trim());
        return str.substring(0, count);
    }

    /**
     * Generate space
     *
     * @param spaces number of spaces
     * @return generated indentation
     */
    public static String buildSpace(int spaces) {
        return build(spaces, " ");
    }

    private static String build(int repetitions, String... elements) {
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < repetitions; i++) {
            for (String element : elements) {
                content.append(element);
            }
        }

        return content.toString();
    }

    /**
     * Amount of the occurrences of the specified text in string
     *
     * @param text    the string to search in
     * @param element the string to search for
     * @return amount of the occurrences
     */
    public static int countOccurrences(String text, String element) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = text.indexOf(element, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += element.length();
            }
        }

        return count;
    }

    /**
     * Checks if the specified text contains any character from the specified array
     *
     * @param text       a string to search
     * @param characters searched characters
     * @return true if the specified string contains any of the specified characters
     */
    public static boolean containsCharacter(String text, char... characters) {
        return contains(text, character -> CharacterUtils.belongsTo(character, characters));
    }

    /**
     * Checks if the specified text contains character other than letter or digit
     *
     * @param text the string to search in
     * @return true if the specified string contains a character other than a letter or a digit
     */
    public static boolean containsSpecialCharacters(String text) {
        return contains(text, character -> !Character.isLetterOrDigit(character));
    }

    /**
     * Checks if the specified text contains other characters than these from the specified array
     *
     * @param text the string to search in
     * @return true if the string contains a character other than the specified in arrays
     */
    public static boolean containsOtherCharacters(String text, char[]... characters) {
        return contains(text, character -> !CharacterUtils.belongsTo(character, characters));
    }

    private static boolean contains(String text, Function<Character, Boolean> condition) {
        for (char c : text.toCharArray()) {
            if (!condition.apply(c)) {
                continue;
            }

            return true;
        }

        return false;
    }

    /**
     * Object to string with array support
     *
     * @param object the object to stringify
     * @return object represented by string
     */
    public static String toString(@Nullable Object object) {
        if (object == null) {
            return "null";
        }

        if (object.getClass().isArray()) {
            return ContentJoiner.on(", ").join((Object[]) object).toString();
        }

        return object.toString();
    }

    /**
     * Check if the specified string can be parsed as a number
     * Allowed characters:
     * 0-9 - digits
     * x   - hexadecimal
     * .   - float
     *
     * @param str string to check
     * @return true if the specified text can be a number
     */
    public static boolean isNumber(String str) {
        return contains(str, character -> Character.isDigit(character) || character == 'x' || character == '.');
    }

    /**
     * Checks if the specified text is null or empty, throw exception if it is
     *
     * @param text             to check
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
     * Checks if the specified text is null or empty
     *
     * @param text to check
     * @return true if a specified text is null or empty
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

}
