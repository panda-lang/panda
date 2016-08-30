package org.panda_lang.panda;

import org.panda_lang.core.interpreter.lexer.*;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenHollow;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;

import java.io.File;
import java.util.List;

public class ExtractorTest {

    private static final File SOURCE_FILE = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        Lexer lexer = new PandaLexer(panda, FileUtils.getContentOfFile(SOURCE_FILE));
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        TokenPattern pattern = TokenPattern.builder()
                .unit(TokenType.KEYWORD, "method")
                .hollow()
                .unit(TokenType.SEPARATOR, "(")
                .hollow()
                .unit(TokenType.SEPARATOR, ")")
                .unit(TokenType.SEPARATOR, "{")
                .hollow()
                .unit(TokenType.SEPARATOR, "}")
                .build();

        TokenExtractor extractor = pattern.extractor();
        boolean matched = extractor.extract(tokenReader);

        if (matched) {
            List<TokenHollow> hollows = extractor.getHollows();

            for (TokenHollow hollow : hollows) {
                System.out.println("--- TokenHollow");

                for (Token token : hollow.getTokens()) {
                    System.out.println(": " + token.toString());
                }
            }
        }

        System.out.println(matched);
    }

}
