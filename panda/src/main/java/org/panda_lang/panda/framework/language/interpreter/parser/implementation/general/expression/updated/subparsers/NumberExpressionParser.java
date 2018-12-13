package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number.NumberParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.number.NumberUtils;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

class NumberExpressionParser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens tokens = new PandaTokens();

        for (TokenRepresentation representation : source.getTokensRepresentations()) {
            if (!NumberUtils.isNumeric(representation.getTokenValue())) {
                break;
            }

            tokens.addToken(representation);
        }

        if (tokens.size() < 2) {
            return null;
        }

        return source;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        NumberParser numberParser = new NumberParser();
        Value numericValue = numberParser.parse(data, source);

        if (numericValue != null) {
            return new PandaExpression(numericValue);
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SIMPLE_DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.NUMBER;
    }

}
