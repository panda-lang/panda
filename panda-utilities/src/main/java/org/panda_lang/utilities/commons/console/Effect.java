/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.commons.console;

public enum Effect {

    LINE_SEPARATOR("\n", "n"),

    RESET("\033[0m", "r"),
    BOLD("\033[0;1m", "b"),

    BLACK("\033[0;30m", "0"),
    RED("\033[0;31m", "1"),
    GREEN("\033[0;32m", "2"),
    YELLOW("\033[0;33m", "3"),
    BLUE("\033[0;34m", "4"),
    MAGENTA("\033[0;35m", "5"),
    CYAN("\033[0;36m", "6"),
    WHITE("\033[0;37m", "7"),

    BLACK_BOLD("\033[1;30m", "0b"),
    RED_BOLD("\033[1;31m", "1b"),
    GREEN_BOLD("\033[1;32m", "2b"),
    YELLOW_BOLD("\033[1;33m", "3b"),
    BLUE_BOLD("\033[1;34m", "4b"),
    MAGENTA_BOLD("\033[1;35m", "5b"),
    CYAN_BOLD("\033[1;36m", "6b"),
    WHITE_BOLD("\033[1;37m", "7b"),

    BLACK_UNDERLINED("\033[4;30m", "0u"),
    RED_UNDERLINED("\033[4;31m", "1u"),
    GREEN_UNDERLINED("\033[4;32m", "2u"),
    YELLOW_UNDERLINED("\033[4;33m", "3u"),
    BLUE_UNDERLINED("\033[4;34m", "4u"),
    MAGENTA_UNDERLINED("\033[4;35m", "5u"),
    CYAN_UNDERLINED("\033[4;36m", "6u"),
    WHITE_UNDERLINED("\033[4;37m", "7u");

    /*
    BLACK_BACKGROUND("\033[40m", ""),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    MAGENTA_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    WHITE_BACKGROUND("\033[47m"),

    BLACK_BRIGHT("\033[0;90m"),
    RED_BRIGHT("\033[0;91m"),
    GREEN_BRIGHT("\033[0;92m"),
    YELLOW_BRIGHT("\033[0;93m"),
    BLUE_BRIGHT("\033[0;94m"),
    MAGENTA_BRIGHT("\033[0;95m"),
    CYAN_BRIGHT("\033[0;96m"),
    WHITE_BRIGHT("\033[0;97m"),

    BLACK_BOLD_BRIGHT("\033[1;90m"),
    RED_BOLD_BRIGHT("\033[1;91m"),
    GREEN_BOLD_BRIGHT("\033[1;92m"),
    YELLOW_BOLD_BRIGHT("\033[1;93m"),
    BLUE_BOLD_BRIGHT("\033[1;94m"),
    MAGENTA_BOLD_BRIGHT("\033[1;95m"),
    CYAN_BOLD_BRIGHT("\033[1;96m"),
    WHITE_BOLD_BRIGHT("\033[1;97m"),

    BLACK_BACKGROUND_BRIGHT("\033[0;100m"),
    RED_BACKGROUND_BRIGHT("\033[0;101m"),
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),
    BLUE_BACKGROUND_BRIGHT("\033[0;104m"),
    MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"),
    CYAN_BACKGROUND_BRIGHT("\033[0;106m"),
    WHITE_BACKGROUND_BRIGHT("\033[0;107m");
    */

    private final String code;
    private final String simpleCode;

    Effect(String code, String simpleCode) {
        this.code = code;
        this.simpleCode = simpleCode;
    }

    public String getSimpleCode() {
        return simpleCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getCode();
    }

}
