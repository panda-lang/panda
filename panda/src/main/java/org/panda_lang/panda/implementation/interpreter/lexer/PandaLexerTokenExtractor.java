package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.util.StringUtils;
import org.panda_lang.panda.composition.SyntaxComposition;
import org.panda_lang.panda.implementation.interpreter.token.PandaToken;

import java.util.Collection;

public class PandaLexerTokenExtractor {

    private final PandaLexer lexer;
    private final SyntaxComposition syntaxComposition;

    public PandaLexerTokenExtractor(PandaLexer lexer) {
        this.lexer = lexer;
        this.syntaxComposition = lexer.getSyntaxComposition();
    }

    protected boolean extract(StringBuilder tokenBuilder) {
        String tokenPreview = tokenBuilder.toString();

        while (tokenPreview.length() != 0) {
            tokenPreview = tokenPreview.trim();

            if (tokenPreview.isEmpty()) {
                return true;
            }

            Token token = extractToken(tokenPreview, syntaxComposition.getSeparators(), syntaxComposition.getOperators(), syntaxComposition.getKeywords());

            if (token == null) {
                if (StringUtils.containsCharacter(tokenPreview, syntaxComposition.getSpecialCharacters())) {
                    return false;
                }

                token = new PandaToken(TokenType.UNKNOWN, tokenPreview);
            }

            lexer.getTokenizedLine().add(token);
            tokenBuilder.delete(0, token.getToken().length());
            tokenPreview = tokenBuilder.toString();
        }

        return true;
    }

    @SafeVarargs
    protected final Token extractToken(String tokenPreview, Collection<? extends Token>... tokensCollections) {
        for (Collection<? extends Token> tokensCollection : tokensCollections) {
            for (Token token : tokensCollection) {
                if (!tokenPreview.startsWith(token.getToken())) {
                    continue;
                }

                return token;
            }
        }

        return null;
    }

}
