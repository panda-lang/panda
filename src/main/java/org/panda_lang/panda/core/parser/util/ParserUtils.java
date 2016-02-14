package org.panda_lang.panda.core.parser.util;

import java.util.List;

public class ParserUtils {

    public static String toString(List<String> list, String between, int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < end; i++) {
            stringBuilder.append(list.get(i));

            if (between != null && i != end - 1) {
                stringBuilder.append(between);
            }
        }
        return stringBuilder.toString();
    }

}
