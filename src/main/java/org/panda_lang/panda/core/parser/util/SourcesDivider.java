package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.core.statement.Token;

public class SourcesDivider {

    private final char[] source;
    private final Token token;
    private int index, iLine, realLine;
    private StringBuilder node;
    private String line;

    public SourcesDivider(String source) {
        source = source.replace(System.lineSeparator(), Character.toString('\n'));
        this.source = source.toCharArray();
        this.token = new Token();
        this.node = new StringBuilder();
        this.index = -1;
    }

    public String next() {
        boolean end = false;
        boolean string = false;
        boolean skip = false;

        ++index;
        for (; index < source.length; index++) {
            char c = source[index];

            if (c == '\n') {
                realLine++;
                skip = false;
                continue;
            }
            if (node.length() == 0 && Character.isWhitespace(c)) {
                continue;
            }
            else if (node.length() == 0 && c == '/' && source[index + 1] == '/') {
                skip = true;
            }

            if (skip) {
                continue;
            }

            if (c == token.STRING_TOKEN) {
                string = !string;
            }
            else if (c == token.BLOCK_START_TOKEN || c == token.BLOCK_END_TOKEN || c == token.STATEMENT_SEPARATOR_TOKEN) {
                end = true;
            }

            node.append(c);

            if (end) {
                iLine++;
                line = node.toString();
                node.setLength(0);
                return line;
            }
        }
        return node.toString();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean hasNext() {
        return index < source.length - 1;
    }

    public int getLineNumber() {
        return iLine;
    }

    public int getRealLine() {
        return realLine;
    }

    public int getCaretPosition() {
        return index;
    }

    public Token getToken() {
        return token;
    }

    public char[] getSource() {
        return source;
    }

}
