package org.panda_lang.core.util;

public class StringUtils {

    public static final String EMPTY = "";

    public static String replace(String text, String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString.isEmpty()) {
            return text;
        }

        if (replacement == null) {
            replacement = "";
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
