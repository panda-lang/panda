package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.suggestion.Sequence;
import org.panda_lang.panda.implementation.interpreter.token.PandaToken;

import java.util.Collection;
import java.util.Stack;

public class PandaLexerSequencer {

    private final PandaLexer lexer;
    private final Collection<Sequence> sequences;
    private final Stack<Sequence> sequenceStack;

    public PandaLexerSequencer(PandaLexer lexer, Collection<Sequence> sequences) {
        this.lexer = lexer;
        this.sequences = sequences;
        this.sequenceStack = new Stack<>();
    }

    public boolean checkBefore(StringBuilder tokenBuilder, char c) {
        if (sequenceStack.size() > 0) {
            String tokenPreview = lexer.getTokenBuilder().append(c).toString();
            Sequence sequence = sequenceStack.peek();

            if (!tokenPreview.endsWith(sequence.getSequenceEnd())) {
                return true;
            }

            Token token = new PandaToken(TokenType.SEQUENCE, tokenPreview);
            lexer.getTokenizedSourceLine().add(token);

            tokenBuilder.setLength(0);
            sequenceStack.pop();
            return true;
        }

        return false;
    }

    public boolean checkAfter(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        for (Sequence sequence : sequences) {
            if (!tokenPreview.startsWith(sequence.getSequenceStart())) {
                continue;
            }

            sequenceStack.push(sequence);
            return true;
        }

        return false;
    }

}
