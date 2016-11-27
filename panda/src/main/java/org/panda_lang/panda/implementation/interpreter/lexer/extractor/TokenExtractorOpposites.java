package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.suggestion.Separator;
import org.panda_lang.panda.implementation.syntax.Separators;

import java.util.Stack;

public class TokenExtractorOpposites {

    private final Stack<Separator> separators;
    private final boolean active;

    protected TokenExtractorOpposites(TokenExtractor tokenExtractor) {
        this.separators = new Stack<>();
        this.active = tokenExtractor.getPattern().isKeepingOpposites();
    }

    public void report(Token token) {
        if (!active) {
            return;
        }

        Separator separator = Separators.valueOf(token);

        if (separator == null) {
            return;
        }

        if (separators.size() > 0) {
            Separator previousSeparator = separators.peek();
            Separator opposite = previousSeparator.getOpposite();

            if (separator.equals(opposite)) {
                separators.pop();
                return;
            }
        }

        if (!separator.hasOpposite()) {
            return;
        }

        separators.push(separator);
    }

    public boolean isLocked() {
        return separators.size() > 0;
    }

}
