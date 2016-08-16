package org.panda_lang.core.util;

public class CharacterUtils {

    public static boolean belongsTo(char character, char... characters) {
        for (char c : characters) {
            if (c == character) {
                return true;
            }
        }

        return false;
    }

}
