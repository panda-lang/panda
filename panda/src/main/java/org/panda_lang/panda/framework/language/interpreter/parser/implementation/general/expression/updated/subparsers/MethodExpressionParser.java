package org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
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

public class MethodExpressionParser implements ExpressionSubparser {

    private static final Token[] METHOD_SEPARATORS = ArrayUtils.of(Separators.PERIOD);
    private final boolean voids;

    public MethodExpressionParser(boolean voids) {
        this.voids = voids;
    }

    public MethodExpressionParser() {
        this(false);
    }

    @Override
    public @Nullable Tokens read(ExpressionParser main, Tokens source) {
        Tokens selected = SubparserUtils.readDotted(main, source, METHOD_SEPARATORS, matchable -> {
            // at least 3 elements required: <method-name> ( )
            if (matchable.getDistributor().size() - matchable.getIndex() < 3) {
                return false;
            }

            // read method name
            matchable.nextVerified();

            if (!matchable.nextVerified().contentEquals(Separators.LEFT_PARENTHESIS_DELIMITER)) {
                return false;
            }

            // parameters content
            while (matchable.hasNext() && !matchable.isMatchable()) {
                matchable.nextVerified();
            }

            if (!matchable.isMatchable()) {
               return false;
            }

            return matchable.next().contentEquals(Separators.RIGHT_PARENTHESIS_DELIMITER);
        });

        // at least 3 elements required: <method-name> ( )
        if (selected == null || selected.size() < 3 ) {
            return null;
        }

        // method source has to end with parenthesis
        if (!selected.getLast().contentEquals(Separators.RIGHT_PARENTHESIS_DELIMITER)) {
            return null;
        }

        // verify period-less structure
        if (!selected.contains(Separators.PERIOD) && !selected.get(1).contentEquals(Separators.LEFT_PARENTHESIS_DELIMITER)) {
            return null;
        }

        return selected;
    }

    @Override
    public Expression parse(ExpressionParser main, ParserData data, Tokens source) {
        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(source);

        if (methodInvokerParser == null) {
            return null;
        }

        methodInvokerParser.parse(source, data);
        MethodInvokerExpressionCallback callback = methodInvokerParser.toCallback();

        return new PandaExpression(callback.getReturnType(), callback);
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.DYNAMIC;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.METHOD;
    }

}
