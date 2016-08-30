package org.panda_lang.panda;

import org.panda_lang.core.interpreter.lexer.Lexer;
import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;

import java.io.File;

public class LexerTest {

    private static final File SOURCE_FILE = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        Lexer lexer = new PandaLexer(panda, FileUtils.getContentOfFile(SOURCE_FILE));
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        for (Token token : tokenReader) {
            System.out.println((tokenReader.getIteratorLine() + 1) + "[" + tokenReader.getIteratorIndex() + "]" + ": " + token.getType().toString() + ": " + token.getToken());
        }
    }

}
