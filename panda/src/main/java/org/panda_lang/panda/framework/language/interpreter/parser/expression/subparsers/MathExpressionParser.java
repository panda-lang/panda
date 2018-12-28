package org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.callbacks.math.MathUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

class MathExpressionParser implements ExpressionSubparser {

    private static final MathParser MATH_PARSER = new MathParser();

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readSeparated(main, source, MathUtils.MATH_OPERATORS, null, matchable -> {
            if (!matchable.hasNext()) {
                return false;
            }

            Tokens subSource = matchable.currentSubSource();

            if (subSource.isEmpty()) {
                return false;
            }

            Tokens lastExpression = main.read(subSource);

            if (lastExpression == null) {
                return false;
            }

            matchable.getDistributor().next(lastExpression.size());
            return true;
        });

        if (selected == null) {
            return null;
        }

        if (!TokensUtils.contains(selected, MathUtils.MATH_OPERATORS)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        if (!MathExpressionUtils.isMathExpression(source)) {
            return null;
        }

        MathExpressionCallback expression = MATH_PARSER.parse(source, data);
        return new PandaExpression(expression.getReturnType(), expression);
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.ADVANCED_DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.MATH;
    }

}
