package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.redact.Fragment;
import org.panda_lang.core.interpreter.parser.redact.divider.Divider;
import org.panda_lang.core.work.executable.Executable;
import org.panda_lang.core.work.executable.Wrapper;
import org.panda_lang.panda.lang.interpreter.parser.util.PandaParserAssistant;

public class PandaParser {

    private final Wrapper wrapper;
    private final Divider divider;

    public PandaParser(String sourceCode) {
        this.wrapper = new Wrapper();
        this.divider = PandaParserAssistant.getDefaultDivider(sourceCode);
    }

    public Wrapper parse(ParserInfo parserInfo) {
        for (Fragment fragment : divider) {
            Executable executable = parseFragment(fragment);
            wrapper.addExecutable(executable);
        }
        return wrapper;
    }

    public Executable parseFragment(Fragment fragment) {
        return null;
    }

}
