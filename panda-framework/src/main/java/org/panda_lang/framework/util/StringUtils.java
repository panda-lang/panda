/*
 * Copyright (c) 2015-2016 Dzikoysk
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

package org.panda_lang.framework.util;

public class StringUtils {

    public static final String EMPTY = "";

    /**
     * Faster alternative to str.replace
     */
    public static String replace(String text, String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString.isEmpty()) {
            return text;
        }

        if (replacement == null) {
            replacement = EMPTY;
        }

        int start = 0;
        int max = -1;
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
            sb.append(text.substring(start, end)).append(replacement);
            start = end + replaceLength;

            if (--max == 0) {
                break;
            }

            end = text.indexOf(searchString, start);
        }

        sb.append(text.substring(start));
        return sb.toString();
    }

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

    public static boolean containsSpecialCharacters(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }

        return false;
    }

    public static String toCharCodes(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : str.toCharArray()) {
            stringBuilder.append((int) c);
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

}
