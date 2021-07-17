/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package panda.interpreter.syntax.expressions.subparsers;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.architecture.statement.Scope;
import panda.interpreter.architecture.statement.Variable;
import panda.interpreter.architecture.statement.VariableAccessor;
import panda.interpreter.architecture.statement.VariableData;
import panda.interpreter.architecture.type.signature.Signature;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.parser.expression.ExpressionResult;
import panda.interpreter.parser.expression.ExpressionSubparser;
import panda.interpreter.parser.expression.ExpressionSubparserType;
import panda.interpreter.parser.expression.ExpressionSubparserWorker;
import panda.interpreter.token.SourceStream;
import panda.interpreter.token.TokenInfo;
import panda.interpreter.resource.syntax.TokenTypes;
import panda.interpreter.resource.syntax.keyword.Keywords;
import panda.interpreter.syntax.PandaSourceReader;
import panda.interpreter.syntax.scope.variable.VariableDataInitializer;
import panda.interpreter.syntax.type.SignatureParser;
import panda.interpreter.syntax.type.SignatureSource;
import panda.std.Option;

public final class DeclarationParser implements ExpressionSubparser {

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

        private static final SignatureParser SIGNATURE_PARSER = new SignatureParser();

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> expressionContext, TokenInfo token) {
            if (token.getType() != TokenTypes.UNKNOWN && token.getType() != TokenTypes.KEYWORD) {
                return null;
            }

            SourceStream stream = expressionContext.getSynchronizedSource().getCurrentlyAvailableSource().toStream();
            PandaSourceReader sourceReader = new PandaSourceReader(stream);

            boolean mutable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.MUT)).isDefined();
            boolean nillable = sourceReader.optionalRead(() -> sourceReader.read(Keywords.NIL)).isDefined();
            boolean let = sourceReader.optionalRead(() -> sourceReader.read(Keywords.LET)).isDefined();

            Option<SignatureSource> signatureSource = let
                    ? Option.none()
                    : sourceReader.readSignature();

            if (!let && signatureSource.isEmpty()) {
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

            Option<Signature> signature = Option.none();

            if (signatureSource.isDefined()) {
                try {
                    signature = Option.of(SIGNATURE_PARSER.parse(expressionContext, signatureSource.get(), false, null));
                } catch (PandaParserFailure failure) {
                    return ExpressionResult.error(failure.getMessage(), failure.getIndicatedSource().getSource());
                }
            }

            expressionContext.getSynchronizedSource().next(stream.getReadLength() - 1);
            Context<?> context = expressionContext.toContext();
            Scope scope = context.getScope();

            VariableDataInitializer dataInitializer = new VariableDataInitializer(context, scope);
            VariableData variableData = signature
                    .map(value -> dataInitializer.createVariableData(value, name.get(), mutable, nillable))
                    .orElseGet(() -> dataInitializer.createVariableData(name.get(), mutable, nillable));

            Variable variable = scope.createVariable(variableData);
            return ExpressionResult.of(new VariableExpression(new VariableAccessor(variable)));
        }

    }

}
