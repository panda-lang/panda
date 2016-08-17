package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.Application;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserStatus;

public class PandaParserInfo implements ParserInfo {

    private final Application application;
    private final ParserStatus parserStatus;
    private ParserContext parserContext;

    public PandaParserInfo(Application application) {
        this.application = application;
        this.parserStatus = new PandaParserStatus();
    }

    @Override
    public ParserContext getParserContext() {
        return parserContext;
    }

    @Override
    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public ParserStatus getParserStatus() {
        return parserStatus;
    }

    @Override
    public Application getApplication() {
        return application;
    }

}
