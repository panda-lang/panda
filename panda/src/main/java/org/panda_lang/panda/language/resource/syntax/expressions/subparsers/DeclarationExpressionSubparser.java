package org.panda_lang.panda.language.resource.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Variable;
import org.panda_lang.language.architecture.statement.VariableAccessor;
import org.panda_lang.language.architecture.statement.VariableData;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaSourceReader;
import org.panda_lang.panda.language.resource.syntax.scope.variable.VariableDataInitializer;
import org.panda_lang.panda.language.resource.syntax.type.SignatureSource;
import org.panda_lang.utilities.commons.function.Option;

public final class DeclarationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new Worker().withSubparser(this);
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 2;
    }

    @Override
    public String name() {
        return "declaration";
    }

    private static final class Worker extends AbstractExpressionSubparserWorker implements ExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> expressionContext, TokenInfo token) {
            if (token.getType() != TokenTypes.UNKNOWN && token.getType() != TokenTypes.KEYWORD) {
                return null;
            }

            SourceStream stream = expressionContext.getSynchronizedSource().getCurrentlyAvailableSource().toStream();
            PandaSourceReader sourceReader = new PandaSourceReader(stream);

            boolean mutable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isDefined();
            boolean nillable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isDefined();
            Option<SignatureSource> signatureSource = sourceReader.readSignature();

            if (signatureSource.isEmpty()) {
                if (mutable || nillable) {
                    ExpressionResult.error("Missing variable signature", token.toSnippet());
                }

                return null;
            }

            Option<TokenInfo> name = sourceReader.read(TokenTypes.UNKNOWN);

            if (name.isEmpty()) {
                // Skip variable names read as signatures
                // throw new PandaParserFailure(context, "Missing variable name");
                return null;
            }

            expressionContext.getSynchronizedSource().next(stream.getReadLength() - 1);
            Context<?> context = expressionContext.toContext();
            Scope scope = context.getScope();

            VariableDataInitializer dataInitializer = new VariableDataInitializer(context, scope);
            VariableData variableData = dataInitializer.createVariableData(signatureSource.get(), name.get(), mutable, nillable);

            Variable variable = scope.createVariable(variableData);
            return ExpressionResult.of(new VariableExpression(new VariableAccessor(variable)));

            /*
            context.getSubject().getTransaction().getCommits().add(() -> context.getScope().removeVariable(variable.getName()));
            Expression expression = context.getSubject().getTransaction().getExpression();

            if (!variable.getSignature().isAssignableFrom(expression.getSignature())) {
                throw new PandaParserFailure(context, signatureSource.get().getName(),
                        "Cannot assign " + expression.getSignature() + " to " + variable.getSignature(),
                        "Change variable type or ensure the expression has compatible return type"
                );
            }

            Expression equalizedExpression = ExpressionUtils.equalize(expression, variable.getSignature()).orElseThrow(error -> {
                throw new PandaParserFailure(context, "Incompatible signatures");
            });

            VariableAccessor accessor = new VariableAccessor(variable.initialize());
            Assigner<Variable> assigner = accessor.toAssigner(name.get(), true, equalizedExpression);

            return Option.ofCompleted(assigner);

             */
        }

    }

}
