package org.panda_lang.panda.core.parser.improved;

public class SourcesDivider {

    private char[] source;
    private int index, iLine;
    private StringBuilder node;
    private String line;

    public SourcesDivider(String source) {
        this.source = source.toCharArray();
        this.node = new StringBuilder();
    }

    public String next() {
        boolean end = false;
        boolean string = false;
        for (; index < source.length; index++) {
            char c = source[index];

            switch (c) {
                case '"':
                    string = !string;
                    break;
                case '{':
                case '}':
                case ';':
                    end = true;
                    break;
                case '\n':
                case '\r':
                    continue;
            }
            node.append(c);

            if (end) {
                iLine++;
                line = node.toString();
                node.setLength(0);
                return line;
            }
        }
        return null;
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

    public int getCaretPosition() {
        return index;
    }

    public char[] getSource() {
        return source;
    }

}
