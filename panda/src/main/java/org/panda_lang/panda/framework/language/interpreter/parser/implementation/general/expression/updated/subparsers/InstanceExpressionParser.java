package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.ExpressionPatterns;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.instance.ConstructorExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.instance.InstanceExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

import java.util.List;

class InstanceExpressionParser implements ExpressionSubparser {

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        return TokenUtils.equals(source.getFirst(), Keywords.NEW) ? source : null;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        List<Tokens> constructorMatches = ExpressionPatterns.INSTANCE_PATTERN.match(new PandaTokenReader(source));

        if (constructorMatches != null && constructorMatches.size() == 3 && constructorMatches.get(2).size() == 0) {
            ConstructorExpressionParser callbackParser = new ConstructorExpressionParser();

            callbackParser.parse(source, data);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.Dynamic.CONSTRUCTOR_CALL;
    }

}
