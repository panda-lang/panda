package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.parser.lexer.Lexer;
import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.TokenType;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Keyword;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Separator;
import org.panda_lang.core.interpreter.parser.lexer.suggestion.Sequence;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.composition.SyntaxComposition;

import java.util.*;

public class PandaLexer implements Lexer {

    private final Panda panda;
    private final List<Token> tokens;
    private final Collection<Keyword> keywords;
    private final Collection<Separator> separators;
    private final Collection<Sequence> sequences;
    private final StringBuilder tokenBuilder;
    private final Stack<Sequence> sequenceStack;
    private final char[] sourceCharArray;
    private boolean previousSpecial;
    private int index;

    public PandaLexer(Panda panda, String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }
        else if (source.isEmpty()) {
            throw new IllegalArgumentException("Source is empty");
        }

        this.panda = panda;
        this.tokens = new ArrayList<>();
        this.sourceCharArray = source.toCharArray();

        PandaComposition pandaComposition = panda.getPandaComposition();
        SyntaxComposition syntaxComposition = pandaComposition.getSyntaxComposition();

        this.keywords = syntaxComposition.getKeywords();
        this.separators = syntaxComposition.getSeparators();
        this.sequences = syntaxComposition.getSequences();

        this.tokenBuilder = new StringBuilder();
        this.sequenceStack = new Stack<>();
        this.index = 0;
    }

    @Override
    public Token[] convert() {
        for (index = 0; index < sourceCharArray.length; index++) {
            next(sourceCharArray[index]);
        }
        extract();

        return tokens.toArray(new Token[tokens.size()]);
    }

    private void next(char c) {
        if (sequenceStack.size() > 0) {
            tokenBuilder.append(c);
            checkSequence();
            return;
        }

        if (Character.isWhitespace(c)) {
            extract();
            return;
        }

        tokenBuilder.append(c);
        String tokenPreview = tokenBuilder.toString();

        boolean special = !Character.isLetterOrDigit(c);

        if (previousSpecial && !special) {
            extract();
        }
        else if (!previousSpecial && special) {
            extract();
        }

        previousSpecial = special;
    }

    private void extract() {
        String tokenPreview = tokenBuilder.toString();


    }

    private void checkSequence() {
        Sequence sequence = sequenceStack.peek();
        String sequencePreview = tokenBuilder.toString();

        if (!sequencePreview.endsWith(sequence.getSequenceEnd())) {
            return;
        }

        Token token = new PandaToken(TokenType.SEQUENCE, sequencePreview);

        tokenBuilder.setLength(0);
        sequenceStack.pop();
        tokens.add(token);
    }

}
