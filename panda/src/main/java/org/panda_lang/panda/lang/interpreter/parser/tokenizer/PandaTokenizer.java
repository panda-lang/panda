package org.panda_lang.panda.lang.interpreter.parser.tokenizer;

import org.panda_lang.core.interpreter.parser.redact.Sequence;
import org.panda_lang.core.interpreter.parser.tokenizer.Tokenizer;
import org.panda_lang.panda.lang.syntax.PandaSeparator;
import org.panda_lang.panda.lang.syntax.PandaSequence;

import java.util.*;

public class PandaTokenizer implements Tokenizer<PandaToken> {

    private final char[] source;
    private final StringBuilder tokenBuilder;
    private final StringBuilder sequenceBuilder;
    private final Stack<Sequence> sequences;
    private PandaToken previousToken;
    private PandaToken currentToken;
    private int inline;
    private int caret;
    private int line;

    public PandaTokenizer(String source) {
        this.source = source.toCharArray();
        this.tokenBuilder = new StringBuilder();
        this.sequenceBuilder = new StringBuilder();
        this.sequences = new Stack<>();
        this.caret = -1;
        this.line = 1;
    }

    @Override
    public PandaToken next() {
        inline = 0;
        caret++;

        tokenizer:
        for (; caret < source.length; caret++, inline++) {
            char c = source[caret];

            if (Character.isWhitespace(c) && tokenBuilder.length() == 0 && sequences.size() == 0) {
                continue;
            }

            if (sequences.size() > 0) {
                Sequence sequence = sequences.peek();

                sequenceBuilder.append(c);
                String sequencePreview = tokenBuilder.toString();

                if (sequencePreview.endsWith(System.lineSeparator())) {
                    line++;
                }

                if (!sequencePreview.endsWith(sequence.getSequenceEnd())) {
                    continue;
                }

                sequenceBuilder.setLength(0);
                sequences.pop();

                if (sequence.isNegligible()) {
                    continue;
                }

                tokenBuilder.append(sequencePreview);
                break;
            }

            tokenBuilder.append(c);
            String tokenPreview = tokenBuilder.toString();

            if (tokenPreview.endsWith(System.lineSeparator())) {
                line++;
            }



            for (String pandaSeparator : PandaSeparator.values()) {
                if (tokenPreview.equals(pandaSeparator)) {
                    break tokenizer;
                }
            }

            for (PandaSequence pandaSequence : PandaSequence.values()) {
                Sequence sequence = pandaSequence.getSequence();

                if (!tokenPreview.endsWith(sequence.getSequenceStart())) {
                    continue;
                }

                sequences.push(sequence);
                sequenceBuilder.append(sequence.getSequenceStart());
            }
        }

        PandaToken pandaToken = new PandaToken(tokenBuilder.toString());
        sequenceBuilder.setLength(0);
        tokenBuilder.setLength(0);
        sequences.clear();

        previousToken = currentToken;
        currentToken = pandaToken;

        return pandaToken;
    }

    @Override
    public Iterator<PandaToken> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return caret + 1 < source.length;
    }

    public PandaToken getPreviousToken() {
        return previousToken;
    }

    public PandaToken getCurrentToken() {
        return currentToken;
    }

    public int getInline() {
        return inline;
    }

    @Override
    public int getCaretPosition() {
        return caret;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public char[] getSource() {
        return source;
    }

}
