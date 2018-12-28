package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separator;
import org.panda_lang.panda.utilities.commons.CharacterUtils;

import java.util.function.Predicate;

class SubparserUtils {

    public static Predicate<Token> NAMES_FILTER = token -> token.getType() == TokenType.UNKNOWN && !isAllowedName(token);

    private static final char[] ALLOWED_START = CharacterUtils.mergeArrays(
            CharacterUtils.LETTERS,
            CharacterUtils.arrayOf('$', '_')
    );

    private static final char[] ALLOWED = CharacterUtils.mergeArrays(
            ALLOWED_START,
            CharacterUtils.DIGITS
    );

    static @Nullable Tokens readFirstOfType(Tokens source, TokenType type) {
        TokenRepresentation token = source.get(0);

        if (token.getToken().getType() != type) {
            return null;
        }

        return new PandaTokens(token);
    }

    static @Nullable Tokens readBetweenSeparators(Tokens source, Separator first) {
        if (!source.startsWith(first)) {
            return null;
        }

        MatchableDistributor matchable = new MatchableDistributor(new TokenDistributor(source));
        matchable.verify();

        // at least 1 element required
        matchable.nextVerified();

        while (!matchable.isMatchable() && matchable.hasNext()) {
            matchable.nextVerified();
        }

        if (!matchable.isMatchable()) {
            return null;
        }

        return source.subSource(0, matchable.getIndex() + 1);
    }

    static @Nullable Tokens readSeparated(ExpressionParser main, Tokens source, Token[] separators, @Nullable Predicate<Token> filter, DottedFinisher finisher) {
        TokenDistributor distributor = new TokenDistributor(source);
        int lastIndexOfPeriod = -1;

        MatchableDistributor matchable = new MatchableDistributor(distributor);
        matchable.verify();

        while (matchable.hasNext()) {
            TokenRepresentation representation = matchable.next();
            matchable.verify();

            if (!matchable.isMatchable()) {
                continue;
            }

            if (!TokenUtils.contains(separators, representation.getToken())) {
                if (filter != null && filter.test(representation.getToken())) {
                    break;
                }

                continue;
            }

            Tokens selected = source.subSource(0, matchable.getIndex() - 1);
            Tokens matched = main.read(selected);

            if (matched == null || matched.size() != selected.size()) {
                break;
            }

            lastIndexOfPeriod = matchable.getIndex() - 1;
        }

        if (lastIndexOfPeriod == -1) {
            return null;
        }

        distributor.setIndex(lastIndexOfPeriod + 1);

        if (!matchable.hasNext()) {
           return null;
        }

        boolean result = finisher.finish(matchable);

        if (!result) {
            return null;
        }

        return source.subSource(0, matchable.getIndex());
    }

    static boolean isAllowedName(Token token) {
        if (token.getType() != TokenType.UNKNOWN) {
            return false;
        }

        if (!CharacterUtils.belongsTo(token.getTokenValue().charAt(0), ALLOWED_START)) {
            return false;
        }

        char[] chars = token.getTokenValue().toCharArray();

        for (int i = 1; i < token.getTokenValue().toCharArray().length; i++) {
            char c = chars[i];

            if (!CharacterUtils.belongsTo(c, ALLOWED)) {
                return false;
            }
        }

        return true;
    }

    interface DottedFinisher {

        boolean finish(MatchableDistributor distributor);

    }

}
