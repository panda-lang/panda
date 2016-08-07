package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.ParserContext;

public class PandaParserContext implements ParserContext {

    private final String source;

    public PandaParserContext(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }

}
