package org.panda_lang.panda;

import org.panda_lang.core.interpreter.parser.lexer.Lexer;
import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.TokenizedSource;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.lang.interpreter.lexer.PandaLexer;

import java.io.File;

public class PandaLexerTest {

    private static final File SOURCE_FILE = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        Lexer lexer = new PandaLexer(panda, FileUtils.getContentOfFile(SOURCE_FILE));
        TokenizedSource tokenizedSource = lexer.convert();

        for (int line = 0; line < tokenizedSource.getTokenizedSource().length; line++) {
            Token[] tokensLine = tokenizedSource.getTokenizedSource()[line];

            for (Token token : tokensLine) {
                System.out.println((line + 1) + ": " + token.getType().toString() + ": " + token.getToken());
            }
        }
    }

}
