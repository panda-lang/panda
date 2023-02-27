package panda.interpreter.token;

public final class Identifier extends Token<String> {

    public Identifier(String value, int line, int caret) {
        super(TokenType.IDENTIFIER, value, line, caret);
    }

    @Override
    public String toSourceString() {
        return getValue();
    }

}
