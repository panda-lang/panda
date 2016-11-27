package org.panda_lang.panda;

import org.panda_lang.framework.interpreter.lexer.Lexer;
import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenExtractor;
import org.panda_lang.panda.implementation.interpreter.lexer.extractor.TokenPattern;

import java.io.File;
import java.util.List;

public class ExtractorTest {

    private static final File SOURCE_FILE = new File("examples/hello_world.panda");

    public static void main(String[] args) {
        PandaFactory pandaFactory = new PandaFactory();
        Panda panda = pandaFactory.createPanda();

        Lexer lexer = new PandaLexer(panda, "rgerg.wergwerg();");
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        TokenPattern pattern = TokenPattern.builder()
                .keepOpposites(true)
                .gap()
                .unit(TokenType.SEPARATOR, ".")
                .gap()
                .unit(TokenType.SEPARATOR, ";")
                .build();

        TokenExtractor extractor = pattern.extractor();

        boolean matched = extractor.extract(tokenReader);
        List<TokenizedSource> hollows = extractor.getGaps();

        for (TokenizedSource hollow : hollows) {
            System.out.println("--- TokenHollow");

            for (TokenRepresentation tokenRepresentation : hollow.getTokensRepresentations()) {
                System.out.println("  : " + tokenRepresentation.toString());
            }
        }

        System.out.println(matched);
    }

}
