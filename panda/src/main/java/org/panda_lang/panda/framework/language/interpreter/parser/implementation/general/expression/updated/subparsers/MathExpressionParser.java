package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.math.MathExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.math.MathExpressionUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.math.MathParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.old.callbacks.math.MathUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionSubparser;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class MathExpressionParser implements ExpressionSubparser {

    private static final MathParser MATH_PARSER = new MathParser();

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readDotted(main, source, MathUtils.MATH_OPERATORS, matchable -> {
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
        return DefaultSubparserPriorities.ADVANCED_DYNAMIC;
    }

}
