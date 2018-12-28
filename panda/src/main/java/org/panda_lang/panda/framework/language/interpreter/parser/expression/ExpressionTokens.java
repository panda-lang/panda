package org.panda_lang.panda.framework.language.interpreter.parser.expression;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

public class ExpressionTokens extends PandaTokens {

    private final ExpressionSubparser subparser;

    public ExpressionTokens(Tokens tokens, ExpressionSubparser subparser) {
        super(tokens.getTokensRepresentations());
        this.subparser = subparser;
    }

    public ExpressionSubparser getSubparser() {
        return subparser;
    }

}
