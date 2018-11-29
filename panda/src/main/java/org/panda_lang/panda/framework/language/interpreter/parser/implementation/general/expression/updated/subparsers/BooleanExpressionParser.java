package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.DefaultSubparserPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

public class BooleanExpressionParser implements ExpressionSubparser {

    @Override
    public Expression parse(ParserData data, Tokens expressionsource) {
        return null;
    }

    @Override
    public @Nullable Tokens read(Tokens source) {
        TokenRepresentation token = source.get(0);

        if (token.getToken().getType() != TokenType.LITERAL) {
            return null;
        }

        if (!ObjectUtils.equalsOneOf(token.getTokenValue(), "true", "false")) {
            return null;
        }

        return new PandaTokens(token);
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.SIMPLE;
    }

}
