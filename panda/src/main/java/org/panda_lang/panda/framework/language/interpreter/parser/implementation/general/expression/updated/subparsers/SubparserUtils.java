package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

class SubparserUtils {

    static @Nullable Tokens readFirstOfType(Tokens source, TokenType type) {
        TokenRepresentation token = source.get(0);

        if (token.getToken().getType() != type) {
            return null;
        }

        return new PandaTokens(token);
    }

    static @Nullable Tokens readDotted(ExpressionParser main, Tokens source, DottedFinisher finisher) {
        TokenDistributor distributor = new TokenDistributor(source);
        MatchableDistributor matchable = new MatchableDistributor(distributor);
        int lastIndexOfPeriod = 0;

        while (matchable.hasNext()) {
            TokenRepresentation representation = distributor.next();
            matchable.verify();

            if (!matchable.isMatchable()) {
                continue;
            }

            if (!representation.contentEquals(Separators.PERIOD)) {
                continue;
            }

            Tokens selected = source.subSource(0, distributor.getIndex() - 1);
            Tokens matched = main.read(selected);

            if (matched == null || matched.size() != selected.size()) {
                break;
            }

            lastIndexOfPeriod = distributor.getIndex();
        }

        distributor.setIndex(lastIndexOfPeriod);
        finisher.finish(matchable);

        return source.subSource(0, distributor.getIndex());
    }

    interface DottedFinisher {

        void finish(MatchableDistributor distributor);

    }

}
