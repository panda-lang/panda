package panda.interpreter.token;

public abstract class Token<VALUE> {

    protected final TokenType type;
    protected final VALUE value;
    protected final int line;
    protected final int caret;

    protected Token(TokenType type, VALUE value, int line, int caret) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.caret = caret;
    }

    public abstract String toSourceString();

    @Override
    public String toString() {
        return super.getClass().getSimpleName() + "@" + value + " " + getLocation() + "}";
    }

    public String getLocation() {
        return "[" + (getLine() + 1) + ":" + getCaret() + "]";
    }

    public TokenType getType() {
        return type;
    }

    public VALUE getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getCaret() {
        return caret;
    }

}
