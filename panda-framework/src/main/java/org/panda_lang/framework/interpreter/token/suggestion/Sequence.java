package org.panda_lang.framework.interpreter.token.suggestion;

import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.interpreter.token.util.EqualableToken;

public class Sequence extends EqualableToken {

    private final String tokenName;
    private final String sequenceStart;
    private final String sequenceEnd;

    public Sequence(String tokenName, char sequence) {
        this(tokenName, Character.toString(sequence));
    }

    public Sequence(String tokenName, String sequence) {
        this(tokenName, sequence, sequence);
    }

    public Sequence(String tokenName, String sequenceStart, String sequenceEnd) {
        this.tokenName = tokenName;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
    }

    public String getSequenceStart() {
        return sequenceStart;
    }

    public String getSequenceEnd() {
        return sequenceEnd;
    }

    @Override
    public String getTokenValue() {
        return getSequenceStart() + getSequenceEnd();
    }

    @Override
    public String getName() {
        return tokenName;
    }

    @Override
    public TokenType getType() {
        return TokenType.SEQUENCE;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getTokenValue();
    }

}
