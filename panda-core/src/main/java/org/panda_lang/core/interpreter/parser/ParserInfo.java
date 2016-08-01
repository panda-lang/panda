package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.Application;

public class ParserInfo {

    private Application application;
    private ParserStatus parserStatus;
    private String source;

    public ParserInfo(Application application) {
        this.application = application;
        this.parserStatus = new ParserStatus();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ParserStatus getParserStatus() {
        return parserStatus;
    }

    public Application getApplication() {
        return application;
    }

}
