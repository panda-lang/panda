package org.panda_lang.panda.design.interpreter.token;

import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.AbyssPatternUtils;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.redactor.AbyssRedactorHollows;

public class AbyssPatternAssistant {

    public static AbyssRedactor traditionalMapping(AbyssPattern pattern, ParserInfo info, String... mapping) {
        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(pattern, info);
        AbyssRedactor redactor = new AbyssRedactor(hollows);
        return redactor.map(mapping);
    }

    public static AbyssRedactorHollows extract(AbyssPattern pattern, ParserInfo parserInfo) {
        return AbyssPatternUtils.extract(pattern, parserInfo.getComponent(Components.SOURCE_STREAM));
    }

}
