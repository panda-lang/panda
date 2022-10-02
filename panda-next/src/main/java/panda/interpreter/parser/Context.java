package panda.interpreter.parser;

import panda.interpreter.compiler.CompiledScript;

public class Context {

    private final ParseQueue parseQueue;

    public Context(ParseQueue parseQueue) {
        this.parseQueue = parseQueue;
    }

    public ParseQueue getParseQueue() {
        return parseQueue;
    }

}
