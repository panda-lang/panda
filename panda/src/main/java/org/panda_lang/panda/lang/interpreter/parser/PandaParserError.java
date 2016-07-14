package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.ParserError;

public class PandaParserError implements ParserError {

    private String title;
    private String[] particulars;
    private int line;

    public PandaParserError title(String title) {
        this.title = title;
        return this;
    }

    public PandaParserError particulars(String... particulars) {
        this.particulars = particulars;
        return this;
    }

    public PandaParserError line(int line) {
        this.line = line;
        return this;
    }

    @Override
    public String getContent() {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("[PandaParserException] Caused by: ");
        contentBuilder.append(title);

        if (particulars == null) {
            contentBuilder.append(" [at line ");
            contentBuilder.append(line);
            contentBuilder.append("]");
            return contentBuilder.toString();
        }

        contentBuilder.append(System.lineSeparator());

        for (int i = 0; i < particulars.length; i++) {
            contentBuilder.append("    ");
            contentBuilder.append(particulars[i]);

            if (i == 0) {
                contentBuilder.append(" [at line ");
                contentBuilder.append(line);
                contentBuilder.append("]");
            }

            contentBuilder.append(System.lineSeparator());
        }

        return contentBuilder.toString();
    }

}
