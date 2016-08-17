package org.panda_lang.panda.composition.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.work.Executable;

public class MethodParser implements Parser {

    @Override
    public Executable parse(ParserInfo parserInfo) {
        return null;
    }

    public static class MethodParserHandler implements ParserHandler {

        @Override
        public boolean handle(TokenReader tokenReader) {
            return false;
        }

    }
}
