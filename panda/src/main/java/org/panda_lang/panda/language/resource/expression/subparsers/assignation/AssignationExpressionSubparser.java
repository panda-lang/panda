/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation;

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
import org.panda_lang.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PandaChannel;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.framework.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.resource.expression.subparsers.AbstractExpressionSubparserWorker;

public final class AssignationExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new AssignationExpressionSubparserWorker().withSubparser(this);
    }

    @Override
    public int getMinimalRequiredLengthOfSource() {
        return 3;
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

    private static class AssignationExpressionSubparserWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext expressionContext, TokenRepresentation token) {
            Snippet source = expressionContext.getSynchronizedSource().getSource();
            int index = OperatorUtils.indexOf(source, OperatorFamilies.ASSIGNATION);

            if (index == -1) {
                return null;
            }

            Context context = expressionContext.getContext();
            Snippet declaration = source.subSource(0, index);

            Context assignationContext = context.fork()
                    .withComponent(Components.CHANNEL, new PandaChannel())
                    .withComponent(AssignationComponents.CONTEXT, expressionContext)
                    .withComponent(AssignationComponents.SCOPE, context.getComponent(Components.SCOPE));

            HandleResult<AssignationSubparser> handleResult = context.getComponent(Components.PIPELINE)
                    .getPipeline(PandaPipeline.ASSIGNER)
                    .handle(assignationContext, assignationContext.getComponent(Components.CHANNEL), declaration);

            if (!handleResult.isFound() || !handleResult.getParser().isPresent()) {
                return null;
            }

            SourceStream expressionSource = new PandaSourceStream(source.subSource(index + 1, source.size()));

            try {
                ExpressionTransaction expression = expressionContext.getParser().parse(assignationContext, expressionSource);

                ExpressionResult result = handleResult.getParser().get().parseAssignment(assignationContext, declaration, expression.getExpression());
                expressionContext.getSynchronizedSource().setIndex(declaration.size() + 1 + expressionSource.getReadLength());

                return result;
            } catch (PandaExpressionParserFailure e) {
                return ExpressionResult.error("Cannot parse assigned expression - " + e.getExpressionMessage(), expressionSource.getOriginalSource());
            } catch (Exception e) {
                return ExpressionResult.error("Cannot parse assigned expression - " + e.getMessage(), expressionSource.getOriginalSource());
            }
        }

    }

}