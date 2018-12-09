package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.invoker.MethodInvokerExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.invoker.MethodInvokerExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ArrayUtils;

class MethodExpressionParser implements ExpressionSubparser {

    private static final Token[] METHOD_SEPARATORS = ArrayUtils.of(Separators.PERIOD);

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readDotted(main, source, METHOD_SEPARATORS, matchable -> {
            while (matchable.hasNext()) {
                TokenRepresentation representation = matchable.next();
                matchable.verify();

                if (!matchable.isMatchable()) {
                    continue;
                }

                if (representation.contentEquals(Separators.RIGHT_PARENTHESIS_DELIMITER)) {
                    break;
                }
            }

            return true;
        });

        if (selected == null || selected.size() < 3 ) {
            return null;
        }

        if (!selected.getLast().contentEquals(Separators.RIGHT_PARENTHESIS_DELIMITER)) {
            return null;
        }

        if (!selected.contains(Separators.PERIOD) && !selected.get(1).contentEquals(Separators.LEFT_PARENTHESIS_DELIMITER)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(source);

        if (methodInvokerParser != null) {
            methodInvokerParser.parse(source, data);
            MethodInvokerExpressionCallback callback = methodInvokerParser.toCallback();
            return new PandaExpression(callback.getReturnType(), callback);
        }

        return null;
    }

    @Override
    public double getPriority() {
        return DefaultSubparserPriorities.DYNAMIC;
    }

}
