package panda.interpreter.lexer;

class StringStream {

    private final char[] source;
    private int index = 0;
    private int line = 1;
    private int caret = -1;

    protected StringStream(char[] source) {
        this.source = source;
    }

    public boolean hasContent() {
        return hasContent(0);
    }

    protected boolean hasContent(int offset) {
        return (index + offset) < source.length;
    }

    protected char peek(int offset) {
        return source[index + offset];
    }

    protected char peek() {
        return peek(0);
    }

    protected void next(int amount) {
        for (int index = 0; index < amount; index++) {
            next();
        }
    }

    protected char next() {
        var symbol = source[index++];

        if (symbol == '\n') {
            line++;
            caret = -1;
        }

        return symbol;
    }

    protected String readRemainingLine() {
        return readTo("\n");
    }

    protected String readTo(String to) {
        var line = new StringBuilder();

        while (hasContent()) {
            line.append(next());

            if (line.toString().endsWith(to)) {
                line.setLength(line.length() - to.length());
                break;
            }
        }

        return line.toString();
    }

    protected String getLocation() {
        return "[" + (line + 1) + ":" + caret + "]";
    }

    protected void setCaret(int caret) {
        this.caret = caret;
    }

    protected int getCaret() {
        return caret;
    }

    protected int getIndex() {
        return index;
    }

    protected int getLine() {
        return line;
    }

}
