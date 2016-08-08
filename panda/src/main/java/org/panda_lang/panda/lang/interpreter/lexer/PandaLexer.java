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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class PandaLexer implements Lexer {

    private final Panda panda;

    public PandaLexer(Panda panda) {
        this.panda = panda;
    }

    @Override
    public Token[] convert(String source) {
        if (source == null) {
            throw new IllegalArgumentException("Source cannot be null");
        }

        List<Token> tokens = new ArrayList<>();

        PandaComposition pandaComposition = panda.getPandaComposition();
        SyntaxComposition syntaxComposition = pandaComposition.getSyntaxComposition();

        Collection<Keyword> keywords = syntaxComposition.getKeywords();
        Collection<Separator> separators = syntaxComposition.getSeparators();
        Collection<Sequence> sequences = syntaxComposition.getSequences();

        StringBuilder tokenBuilder = new StringBuilder();
        Stack<Sequence> sequenceStack = new Stack<>();

        char[] sourceCharArray = (source + " ").toCharArray();

        lexer:
        for (int i = 0; i < sourceCharArray.length; i++) {
            char c = sourceCharArray[i];

            if (sequenceStack.size() != 0) {
                String tokenPreview = tokenBuilder.append(c).toString();

                for (Sequence sequence : sequences) {
                    if (!tokenPreview.endsWith(sequence.getSequenceEnd())) {
                        continue;
                    }

                    Token token = new PandaToken(TokenType.SEQUENCE, tokenPreview);
                    tokens.add(token);

                    tokenBuilder.setLength(0);
                    sequenceStack.pop();
                    continue lexer;
                }

                continue;
            }

            if (Character.isWhitespace(c)) {
                continue;
            }

            tokenBuilder.append(c);

            if (sequenceStack.size() != 0) {
                continue;
            }

            String tokenPreview = tokenBuilder.toString();

            for (Separator separator : separators) {
                if (!tokenPreview.equals(separator.getToken())) {
                    continue;
                }

                tokenBuilder.setLength(0);
                tokens.add(separator);
                continue lexer;
            }

            for (Keyword keyword : keywords) {
                if (!tokenPreview.equals(keyword.getToken())) {
                    continue;
                }

                tokenBuilder.setLength(0);
                tokens.add(keyword);
                continue lexer;
            }

            for (Sequence sequence : sequences) {
                if (!tokenPreview.equals(sequence.getSequenceStart())) {
                    continue;
                }

                sequenceStack.push(sequence);
                continue lexer;
            }

        }

        return tokens.toArray(new Token[tokens.size()]);
    }

}
