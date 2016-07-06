package org.panda_lang.panda.lang.syntax;

import org.panda_lang.core.element.Separator;

public enum PandaSeparator {

    BLOCK_START('{'),
    BLOCK_END('}'),
    END_LINE(';'),
    SEPARATOR(',');

    private final Separator separator;

    PandaSeparator(char separator) {
        this.separator = new Separator(separator);
    }

    public Separator getSeparator() {
        return separator;
    }

}
