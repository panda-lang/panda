package org.panda_lang.panda.utilities.commons;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ObjectUtils {

    public static boolean isNotNull(@Nullable Object object) {
        return object != null;
    }

    public static boolean areNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }

        return true;
    }

    public static boolean equalsOneOf(Object value, Object... expected) {
        for (Object expectedValue : expected) {
            if (Objects.equals(value, expectedValue)) {
                return true;
            }
        }

        return false;
    }

}
