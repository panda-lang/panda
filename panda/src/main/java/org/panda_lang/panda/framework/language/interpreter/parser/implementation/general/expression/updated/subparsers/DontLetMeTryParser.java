package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

class DontLetMeTryParser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(Tokens source) {
        return source.size() > 1 ? null : new PandaTokens();
    }

    @Override
    public Expression parse(ParserData data, Tokens source) {
        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.AFTER_SINGLE;
    }

}
