/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.parser.pool.PoolParser;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.language.interpreter.parser.PandaTargets;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.AbstractExpressionSubparserWorker;
import org.panda_lang.utilities.commons.UnsafeUtils;
import org.panda_lang.utilities.commons.function.Option;

import java.util.concurrent.CompletableFuture;

public final class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context<?> context) {
        return new AssignationExpressionSubparserWorker(context).withSubparser(this);
    }

    @Override
    public int minimalRequiredLengthOfSource() {
        return 3;
    }

    @Override
    public double priority() {
        return -1;
    }

    @Override
    public ExpressionCategory category() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public ExpressionSubparserType type() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String name() {
        return "assignation";
    }

    private static final class AssignationExpressionSubparserWorker extends AbstractExpressionSubparserWorker {

        private final PoolParser<AssignationContext> assignationParser;

        private AssignationExpressionSubparserWorker(Context<?> context) {
            this.assignationParser = context.getPoolService().getPool(PandaTargets.ASSIGNATION).toParser();
        }

        @Override
        public @Nullable ExpressionResult next(ExpressionContext<?> expressionContext, TokenInfo token) {
            Snippet source = expressionContext.getSynchronizedSource().getSource();
            int index = OperatorUtils.indexOf(source, OperatorFamilies.ASSIGNATION);

            if (index == -1) {
                return null;
            }

            Snippet declaration = source.subSource(0, index);

            if (declaration.size() > 16 /* implement max declaration size in assignation subparser in the future*/) {
                return null;
            }

            SourceStream expressionSource = new PandaSourceStream(source.subSource(index + 1, source.size()));
            ExpressionTransaction expression;

            try {
                expression = expressionContext.getParser().parse(expressionContext, expressionSource);
            } catch (PandaParserFailure e) {
                // throw e; we can't throw because as individual subparser we don't know everything
                // TODO: Support notes/failures by expression results
                return ExpressionResult.error(e.getMessage() + e.getNote().map(note -> ". Note: " + note).orElseGet(""), expressionSource.getOriginalSource());
            } catch (Throwable e) {
                return UnsafeUtils.throwException(e);
            }

            Context<AssignationContext> assignationContext = expressionContext.toContext().forkCreator()
                    .withSource(declaration)
                    .withScriptSource(declaration)
                    .withSubject(new AssignationContext(expression))
                    .toContext();

            Option<? extends CompletableFuture<?>> declarationResult = assignationParser.parseNext(assignationContext);

            if (declarationResult.isEmpty()) {
                return ExpressionResult.error("Unrecognized declaration", declaration);
            }

            int assignationLength = declaration.size() + 1 + expressionSource.getReadLength();
            expressionContext.getSynchronizedSource().setIndex(assignationLength);

            return ExpressionResult.empty();
        }

    }

}