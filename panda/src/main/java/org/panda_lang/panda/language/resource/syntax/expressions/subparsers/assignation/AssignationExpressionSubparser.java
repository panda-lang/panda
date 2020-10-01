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
import org.panda_lang.language.Failure;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.AbstractExpressionSubparserWorker;
import org.panda_lang.utilities.commons.UnsafeUtils;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

public final class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker(Context context) {
        return new AssignationExpressionSubparserWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 3;
    }

    @Override
    public double getPriority() {
        return -1;
    }

    @Override
    public ExpressionCategory getCategory() {
        return ExpressionCategory.STANDALONE;
    }

    @Override
    public ExpressionSubparserType getSubparserType() {
        return ExpressionSubparserType.INDIVIDUAL;
    }

    @Override
    public String getSubparserName() {
        return "assignation";
    }

    private static final class AssignationExpressionSubparserWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext expressionContext, TokenInfo token) {
            Snippet source = expressionContext.getSynchronizedSource().getSource();
            int index = OperatorUtils.indexOf(source, OperatorFamilies.ASSIGNATION);

            if (index == -1) {
                return null;
            }

            Context context = expressionContext.toContext();
            Snippet declaration = source.subSource(0, index);

            if (declaration.size() > 16 /* implement max declaration size in assignation subparser in the future*/) {
                return null;
            }

            PandaLocalChannel channel = new PandaLocalChannel(context.getComponent(Components.CHANNEL));
            channel.override("location", declaration.getLocation());

            Context assignationContext = context.fork()
                    .withComponent(Components.CHANNEL, channel)
                    .withComponent(AssignationComponents.CONTEXT, expressionContext)
                    .withComponent(AssignationComponents.SCOPE, context.getComponent(Components.SCOPE));

            Result<AssignationSubparser, Option<Failure>> handleResult = context.getComponent(Components.PIPELINE)
                    .getPipeline(PandaPipeline.ASSIGNER)
                    .handle(assignationContext, assignationContext.getComponent(Components.CHANNEL), declaration);

            if (handleResult.isErr()) {
                return handleResult.getError()
                        .map(failure -> ExpressionResult.error(failure.getMessage(), failure.getIndicatedSource().getIndicated()))
                        .orElseGet(() -> ExpressionResult.error("Unrecognized declaration", declaration));
            }

            SourceStream expressionSource = new PandaSourceStream(source.subSource(index + 1, source.size()));

            try {
                ExpressionTransaction expression = expressionContext.getParser().parse(assignationContext, expressionSource);

                AssignationSubparser<?> assignationSubparser = handleResult.get();
                ExpressionResult result = assignationSubparser.parseAssignment(assignationContext, declaration, source.get(index), expression.getExpression());

                int assignationLength = declaration.size() + 1 + expressionSource.getReadLength();
                expressionContext.getSynchronizedSource().setIndex(assignationLength);

                return result;
            } catch (PandaParserFailure e) {
                // throw e; we can't throw because as individual subparser we don't know everything
                // TODO: Support notes/failures by expression results
                return ExpressionResult.error(e.getMessage() + e.getNote().map(note -> ". Note: " + note).orElseGet(""), expressionSource.getOriginalSource());
            } catch (Throwable e) {
                return UnsafeUtils.throwException(e);
            }
        }

    }

}