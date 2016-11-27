package org.panda_lang.panda.implementation.syntax;

public class Characters {

    private static final char[] SPECIAL_CHARACTERS = "#&'()[]{}*+,-./:;<=>!|`^".toCharArray();

    public static char[] getSpecialCharacters() {
        return SPECIAL_CHARACTERS;
    }

}
