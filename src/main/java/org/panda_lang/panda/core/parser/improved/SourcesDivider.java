package org.panda_lang.panda.core.parser.improved;

public class SourcesDivider {

    private char[] source;
    private int index, iLine, realLine;
    private StringBuilder node;
    private String line;

    public SourcesDivider(String source) {
        source = source.replace(System.lineSeparator(), Character.toString('\n'));
        this.source = source.toCharArray();
        this.node = new StringBuilder();
        this.index = -1;
    }

    public String next() {
        boolean end = false;
        boolean string = false;
        boolean skip = false;

        index++;
        for (; index < source.length; index++) {
            char c = source[index];

            if (c == '\n') {
                realLine++;
                skip = false;
                continue;
            }
            if (node.length() == 0 && Character.isWhitespace(c)) {
                continue;
            } else if (node.length() == 0 && c == '/' && source[index + 1] == '/') {
                skip = true;
            }

            if (skip) {
                continue;
            }

            switch (c) {
                case '"':
                    string = !string;
                    break;
                case '{':
                case '}':
                case ';':
                    end = true;
                    break;
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

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
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

    public char[] getSource() {
        return source;
    }

}
