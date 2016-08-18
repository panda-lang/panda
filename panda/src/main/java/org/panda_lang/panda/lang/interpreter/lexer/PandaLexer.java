package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.Lexer;
import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.util.CharacterUtils;
import org.panda_lang.core.util.StringUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.composition.SyntaxComposition;

import java.util.ArrayList;
import java.util.Collection;

public class PandaLexer implements Lexer {

    private final Panda panda;
    private final String source;
    private final Token[][] tokenizedSource;
    private final Collection<Token> tokenizedSourceLine;
    private final SyntaxComposition syntaxComposition;

    private final StringBuilder tokenBuilder;
    private final PandaLexerTokenExtractor lexerTokenExtractor;
    private final PandaLexerSequencer lexerSequencer;

    private String linePreview;
    private String tokenPreview;
    private boolean previousSpecial;
    private int currentLine;

    public PandaLexer(Panda panda, String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (source.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        this.panda = panda;
        this.source = source + " ";
        this.tokenizedSource = new Token[StringUtils.countOccurrences(source, System.lineSeparator())][];
        this.tokenizedSourceLine = new ArrayList<>();

        PandaComposition pandaComposition = panda.getPandaComposition();
        this.syntaxComposition = pandaComposition.getSyntaxComposition();

        this.lexerTokenExtractor = new PandaLexerTokenExtractor(this);
        this.lexerSequencer = new PandaLexerSequencer(this, syntaxComposition.getSequences());
        this.tokenBuilder = new StringBuilder();

        this.previousSpecial = false;
        this.currentLine = 0;
    }

    @Override
    public TokenizedSource convert() {
        char[] sourceCharArray = source.toCharArray();

        for (char c : sourceCharArray) {
            next(c);
            checkLine();
        }

        return new PandaTokenizedSource(tokenizedSource);
    }

    private void next(char c) {
        linePreview += c;
        tokenPreview = tokenBuilder.toString();

        if (lexerSequencer.checkBefore(tokenBuilder, c)) {
            return;
        }

        if (CharacterUtils.isWhitespace(c)) {
            boolean extracted = lexerTokenExtractor.extract(tokenBuilder);

            if (!extracted) {
                throw new PandaLexerException("Unknown token: " + tokenPreview);
            }

            return;
        }

        check(c);
        tokenBuilder.append(c);
        lexerSequencer.checkAfter(tokenBuilder);
    }

    private void check(char c) {
        boolean special = CharacterUtils.belongsTo(c, syntaxComposition.getSpecialCharacters());

        if (previousSpecial && !special) {
            lexerTokenExtractor.extract(tokenBuilder);
        }
        else if (!previousSpecial && special) {
            lexerTokenExtractor.extract(tokenBuilder);
        }

        previousSpecial = special;
    }

    private void checkLine() {
        if (linePreview.endsWith(System.lineSeparator())) {
            tokenizedSource[currentLine] = tokenizedSourceLine.toArray(new Token[tokenizedSourceLine.size()]);
            tokenizedSourceLine.clear();
            linePreview = "";
            currentLine++;
        }
    }

    protected int getCurrentLine() {
        return currentLine;
    }

    protected String getTokenPreview() {
        return tokenPreview;
    }

    protected StringBuilder getTokenBuilder() {
        return tokenBuilder;
    }

    protected SyntaxComposition getSyntaxComposition() {
        return syntaxComposition;
    }

    protected Collection<Token> getTokenizedSourceLine() {
        return tokenizedSourceLine;
    }

    protected Token[][] getTokenizedSource() {
        return tokenizedSource;
    }

    public String getSource() {
        return source;
    }

    public Panda getPanda() {
        return panda;
    }

}
