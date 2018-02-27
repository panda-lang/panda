package org.panda_lang.panda.framework.language.interpreter.token.pattern;

import org.panda_lang.panda.framework.design.interpreter.token.TokenType;

public class AbyssPatternTokens {

    /**
     * Hollow type
     */
    public static final TokenType HOLLOW_TYPE = new TokenType("HOLLOW");

    /**
     * Multiline gap
     */
    public static final AbyssPatternUnit HOLLOW = new AbyssPatternUnit(HOLLOW_TYPE, "*");

    /**
     * Inline gap
     */
    public static final AbyssPatternUnit SIMPLE_HOLLOW = new AbyssPatternUnit(HOLLOW_TYPE, "**");

}
