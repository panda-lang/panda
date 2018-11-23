package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.updated;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

class MatchableDistributor {

    private final TokenDistributor distributor;
    private final Stack<Separator> separators = new Stack<>();
    private int previousSize = 0;

    MatchableDistributor(TokenDistributor distributor) {
        this.distributor = distributor;
    }

    public TokenRepresentation next() {
        TokenRepresentation next = distributor.next();
        this.previousSize = separators.size();

        if (!TokenUtils.isTypeOf(next, TokenType.SEPARATOR)) {
            return next;
        }

        Separator separator = (Separator) next.getToken();

        if (separator.hasOpposite()) {
            separators.push(separator);
        }
        else if (!separators.isEmpty() && TokenUtils.equals(next, separators.peek().getOpposite())) {
            separators.pop();
        }

        return next;
    }

    public boolean isMatchable() {
        return separators.size() == 0 || previousSize == 0;
    }

    public boolean hasNext() {
        return distributor.hasNext();
    }

    public TokenDistributor getDistributor() {
        return distributor;
    }

}
