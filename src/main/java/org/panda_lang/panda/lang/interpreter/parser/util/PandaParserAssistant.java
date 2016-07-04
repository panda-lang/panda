package org.panda_lang.panda.lang.interpreter.parser.util;

import org.panda_lang.panda.core.interpreter.parser.redact.divider.Divider;
import org.panda_lang.panda.core.interpreter.parser.redact.divider.DividerRules;
import org.panda_lang.panda.lang.syntax.PandaSeparator;
import org.panda_lang.panda.lang.syntax.PandaSequence;

public class PandaParserAssistant {

    public static Divider getDefaultDivider(String source) {
        DividerRules dividerRules = new DividerRules();
        for (PandaSeparator pandaSeparator : PandaSeparator.values()) {
            dividerRules.addLineSeparator(pandaSeparator.getSeparator());
        }
        for (PandaSequence pandaSequence : PandaSequence.values()) {
            dividerRules.addLiteral(pandaSequence.getSequence());
        }

        return new Divider(source, dividerRules);
    }

}
