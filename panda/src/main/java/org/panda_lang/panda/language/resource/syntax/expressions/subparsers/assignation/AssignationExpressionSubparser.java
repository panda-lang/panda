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
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.interpreter.token.TokenInfo;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaChannel;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.AbstractExpressionSubparserWorker;

import java.util.Optional;

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

            Context context = expressionContext.getContext();
            Snippet declaration = source.subSource(0, index);

            if (declaration.size() > 16 /* implement max declaration size in assignation subparser in the future*/) {
                return null;
            }

            Context assignationContext = context.fork()
                    .withComponent(Components.CHANNEL, new PandaChannel())
                    .withComponent(AssignationComponents.CONTEXT, expressionContext)
                    .withComponent(AssignationComponents.SCOPE, context.getComponent(Components.SCOPE));

            Optional<AssignationSubparser> handleResult = context.getComponent(Components.PIPELINE)
                    .getPipeline(PandaPipeline.ASSIGNER)
                    .handle(assignationContext, assignationContext.getComponent(Components.CHANNEL), declaration)
                    .getParser();

            if (!handleResult.isPresent()) {
                return null;
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
                return ExpressionResult.error(e.getMessage() + (e.hasNote() ? ". Note: " + e.getNote() : ""), expressionSource.getOriginalSource());
            } catch (Exception e) {
                // e.printStackTrace(); TODO: Improve expression subparser errors
                throw new PandaParserFailure(context, token, "Cannot parse assigned expression: " + e.toString());
                // return ExpressionResult.error("Cannot parse assigned expression - " + e.getMessage(), expressionSource.getOriginalSource());
            }
        }

    }

}