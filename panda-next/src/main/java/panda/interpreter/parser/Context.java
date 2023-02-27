package panda.interpreter.parser;

import panda.interpreter.lexer.Lexer;

public class Context {

    private final Lexer lexer;
    private final ParseQueue parseQueue;

    public Context(Lexer lexer, ParseQueue parseQueue) {
        this.lexer = lexer;
        this.parseQueue = parseQueue;
    }

    public ParseQueue getParseQueue() {
        return parseQueue;
    }

    public Lexer getLexer() {
        return lexer;
    }

}
