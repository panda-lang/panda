package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.operator.Operators;

public final class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new AssignationWorker().withSubparser(this);
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.MUTUAL;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String name() {
        return "assignation";
    }

    private static final class AssignationWorker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> context, TokenInfo token) {
            if (!token.contentEquals(Operators.ASSIGNMENT)) {
                return null;
            }

            if (!(context.getResults().peek() instanceof AccessorExpression)) {
                return null;
            }

            AccessorExpression accessorExpression = (AccessorExpression) context.getResults().pop();
            Accessor<?> accessor = accessorExpression.getAccessor();
            Variable variable = accessor.getVariable();

            SourceStream valueSource = context.getSynchronizedSource().getAvailableSource().toStream();
            Expression value = context.getParser().parse(context, valueSource);
            context.getSynchronizedSource().next(valueSource.getReadLength());

            if (variable.awaitsSignature()) {
                variable.interfereSignature(value.getSignature());
            }
            else if (!accessor.getSignature().isAssignableFrom(value.getSignature())) {
                throw new PandaParserFailure(context.toContext(), token,
                        "Cannot assign " + value.getSignature() + " to " + accessor.getSignature(),
                        "Change variable type or ensure the expression has compatible return type"
                );
            }

            Expression equalizedExpression = ExpressionUtils.equalize(value, accessor.getSignature()).orElseThrow(error -> {
                throw new PandaParserFailure(context, "Incompatible signatures");
            });

            accessor.getVariable().initialize();
            Assigner<?> assigner = accessor.toAssigner(token, true, equalizedExpression);

            return ExpressionResult.of(assigner.toExpression());
        }

    }

}
