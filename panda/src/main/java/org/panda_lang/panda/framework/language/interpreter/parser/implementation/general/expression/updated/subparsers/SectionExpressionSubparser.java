package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.MatchableDistributor;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

class SectionExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        if (!source.startsWith(Separators.LEFT_PARENTHESIS_DELIMITER)) {
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

    @Override
    public @Nullable Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        return main.parse(data, source.subSource(1, source.size() - 1));
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.DYNAMIC;
    }

}
