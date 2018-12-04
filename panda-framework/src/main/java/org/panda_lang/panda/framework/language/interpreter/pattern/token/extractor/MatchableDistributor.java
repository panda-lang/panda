package org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;

import java.util.Stack;

public class MatchableDistributor {

    private final TokenDistributor distributor;
    private final Stack<Separator> separators = new Stack<>();
    private int previousSize = 0;

    public MatchableDistributor(TokenDistributor distributor) {
        this.distributor = distributor;
    }

    public @Nullable TokenRepresentation verify() {
        TokenRepresentation next = distributor.getNext();
        verify(next);
        return next;
    }

    public void verify(@Nullable TokenRepresentation next) {
        this.previousSize = separators.size();

        if (next == null) {
            return;
        }

        if (!TokenUtils.isTypeOf(next, TokenType.SEPARATOR)) {
            return;
        }

        Separator separator = (Separator) next.getToken();

        if (separator.hasOpposite()) {
            separators.push(separator);
        }
        else if (!separators.isEmpty() && next.contentEquals(separators.peek().getOpposite())) {
            separators.pop();
        }
    }

    public @Nullable void verifyBefore() {
        for (int i = 0; i < distributor.getIndex(); i++) {
            verify(distributor.get(i));
        }
    }

    public TokenRepresentation current() {
        return distributor.current();
    }

    public TokenRepresentation next() {
        return distributor.next();
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
