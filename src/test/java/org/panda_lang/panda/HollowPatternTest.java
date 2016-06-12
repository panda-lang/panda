package org.panda_lang.panda;

import org.panda_lang.panda.core.parser.match.charset.CharsetPattern;
import org.panda_lang.panda.core.parser.match.hollow.HollowPattern;

public class HollowPatternTest {

    private static final String EXPRESSION = "instance.get().method(parameter.get())";

    public static void main(String[] args) {
        HollowPattern hollowPattern = HollowPattern.builder().compile("*.*(*)*").build();
        boolean matched = hollowPattern.match(EXPRESSION);

        System.out.println("[HollowPattern] Matched: " + matched);
        System.out.println("[HollowPattern] " + hollowPattern.getHollows());

        CharsetPattern charsetPattern = new CharsetPattern("*.*(*)");
        charsetPattern.setCharset(new char[]{ '.', '(', ')' });
        matched = charsetPattern.match(EXPRESSION);

        System.out.println("[CharsetPattern] Matched: " + matched);
    }

}
