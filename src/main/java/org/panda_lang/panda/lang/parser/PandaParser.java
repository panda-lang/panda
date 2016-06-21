package org.panda_lang.panda.lang.parser;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.redact.divider.Divider;
import org.panda_lang.panda.core.parser.redact.Fragment;
import org.panda_lang.panda.core.parser.redact.formatter.Formatter;
import org.panda_lang.panda.core.work.Executable;
import org.panda_lang.panda.core.work.Wrapper;
import org.panda_lang.panda.lang.parser.util.PandaParserAssistant;

public class PandaParser {

    private final Wrapper wrapper;
    private final Divider divider;
    private final Formatter formatter;

    public PandaParser(String sourceCode) {
        this.wrapper = new Wrapper();
        this.divider = PandaParserAssistant.getDefaultDivider(sourceCode);
        this.formatter = PandaParserAssistant.getDefaultFormatter();
    }

    public Wrapper parse(ParserInfo parserInfo) {
        for (Fragment fragment : divider) {
            fragment = formatter.format(fragment);

            Executable executable = parseFragment(fragment);
            wrapper.addExecutable(executable);
        }
        return wrapper;
    }

    public Executable parseFragment(Fragment fragment) {
        return null;
    }

}
