package org.panda_lang.panda.core.parser.redact.formatter;

import org.panda_lang.panda.core.parser.redact.Fragment;

public class Formatter {

    private final FormatterRules formatterRules;

    public Formatter(FormatterRules formatterRules) {
        this.formatterRules = formatterRules;
    }

    public Fragment format(Fragment unformatted) {
        StringBuilder stringBuilder = new StringBuilder();

        return null;
    }

    public FormatterRules getFormatterRules() {
        return formatterRules;
    }

}
