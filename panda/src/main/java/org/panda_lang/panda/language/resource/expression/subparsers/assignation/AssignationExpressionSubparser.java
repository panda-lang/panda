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
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionCategory;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserType;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelineComponents;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.language.interpreter.parser.expression.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.panda.language.interpreter.parser.pipeline.PandaChannel;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.resource.syntax.operator.OperatorFamilies;
import org.panda_lang.panda.language.resource.syntax.operator.OperatorUtils;
import org.panda_lang.panda.language.runtime.expression.PandaDynamicExpression;

public class AssignationExpressionSubparser implements ExpressionSubparser {

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
        public @Nullable ExpressionResult next(ExpressionContext context, TokenRepresentation token) {
            Snippet source = context.getDiffusedSource().getSource();
            int index = OperatorUtils.indexOf(source, OperatorFamilies.ASSIGNATION);

            if (index == -1) {
                return null;
            }

            Snippet declaration = source.subSource(0, index);
            SourceStream expressionSource = new PandaSourceStream(source.subSource(index + 1, source.size()));

            Context assignationContext = context.getContext().fork()
                    .withComponent(PipelineComponents.CHANNEL, new PandaChannel())
                    .withComponent(AssignationComponents.SCOPE, context.getContext().getComponent(UniversalComponents.SCOPE));

            HandleResult<AssignationSubparser> handleResult = context.getContext().getComponent(UniversalComponents.PIPELINE)
                    .getPipeline(PandaPipelines.ASSIGNER)
                    .handle(assignationContext, assignationContext.getComponent(PipelineComponents.CHANNEL), declaration);

            if (!handleResult.isFound()) {
                return null;
            }

            try {
                Expression expression = context.getParser().parse(assignationContext, expressionSource);

                @SuppressWarnings("OptionalGetWithoutIsPresent")
                AssignationSubparser subparser = handleResult.getParser().get();
                Assigner<?> assigner = subparser.parseAssignment(assignationContext, declaration, expression);

                if (assigner == null) {
                    return ExpressionResult.error("Cannot parse declaration", declaration);
                }

                context.getDiffusedSource().setIndex(declaration.size() + 1 + expressionSource.getReadLength());
                Assignation assignation = new Assignation(assigner);
                Expression assignationResult = assignation.toExpression();

                return ExpressionResult.of(new PandaDynamicExpression(expression.getReturnType()) {
                    @Override
                    @SuppressWarnings("unchecked")
                    public Object call(Expression expression, Flow flow) {
                        assignation.execute(flow);
                        return assignationResult.evaluate(flow);
                    }
                }.toExpression());
            } catch (PandaExpressionParserFailure e) {
                return ExpressionResult.error("Cannot parse assigned expression: " + e.getExpressionMessage(), expressionSource.getOriginalSource());
            } catch (Exception e) {
                e.printStackTrace();
                return ExpressionResult.error("Cannot parse assigned expression: " + e.getMessage(), expressionSource.getOriginalSource());
            }
        }

    }

}