package org.panda_lang.panda.utilities.commons;

import java.util.Objects;

public class ObjectUtils {

    public static boolean equalsOneOf(Object value, Object... expected) {
        for (Object expectedValue : expected) {
            if (Objects.equals(value, expectedValue)) {
                return true;
            }
        }

        return false;
    }

}
