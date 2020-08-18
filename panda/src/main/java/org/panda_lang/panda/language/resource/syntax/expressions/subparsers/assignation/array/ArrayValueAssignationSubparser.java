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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.expression.ExpressionUtils;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.TokenInfo;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.language.resource.syntax.TokenTypes;
import org.panda_lang.language.resource.syntax.auxiliary.Section;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationComponents;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AutowiredAssignationParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationType;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.Option;

public final class ArrayValueAssignationSubparser extends AutowiredAssignationParser {

    private static final ArrayValueAccessorParser PARSER = new ArrayValueAccessorParser();

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(PandaPipeline.ASSIGNER);
    }

    @Override
    public double priority() {
        return AssignationPriorities.ARRAY_ASSIGNATION;
    }

    @Override
    protected AutowiredInitializer<@Nullable ExpressionResult> initialize(Context context, AutowiredInitializer<@Nullable ExpressionResult> initializer) {
        return initializer;
    }

    @Override
    protected Boolean customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        TokenInfo sectionRepresentation = source.getLast();

        if (sectionRepresentation == null || sectionRepresentation.getType() != TokenTypes.SECTION) {
            return false;
        }

        Section section = sectionRepresentation.toToken();

        if (!section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT)) {
            return false;
        }

        SourceStream expressionSource = new PandaSourceStream(source.subSource(0, source.size() - 1));
        Option<ExpressionTransaction> expressionTransactionValue = context.getComponent(Components.EXPRESSION).parseSilently(context, expressionSource);

        if (!expressionTransactionValue.isPresent()) {
            return false;
        }

        ExpressionTransaction expressionTransaction = expressionTransactionValue.get();

        if (expressionSource.hasUnreadSource()) {
            expressionTransaction.rollback();
            return false;
        }

        channel.allocated("array-instance", expressionTransaction.getExpression());
        return true;
    }

    @Autowired(order = 1)
    public ExpressionResult parse(
        Context context,
        LocalChannel channel,
        @Ctx SourceStream source,
        @Ctx AssignationType type,
        @Ctx TokenInfo operator,
        @Ctx(AssignationComponents.EXPRESSION_LABEL) Expression value
    ) {
        if (type != AssignationType.DEFAULT) {
            throw new PandaParserFailure(context, operator, "Unsupported operator");
        }

        Snippet snippet = source.toSnippet();
        ArrayAccessor accessor = PARSER.parse(context, snippet, channel.get("array-instance", Expression.class), snippet.getLast().toToken());

        if (!accessor.getReturnType().isAssignableFrom(value.getType())) {
            throw new PandaParserFailure(context, snippet,
                    "Invalid value type, cannot assign " + value.getType() + " to the array of " + accessor.getReturnType(),
                    "Make sure that you are assigning the proper value or change type of the array"
            );
        }

        Expression equalizedExpression = ExpressionUtils.equalize(value, accessor.getReturnType());
        return ExpressionResult.of(accessor.toAssignerExpression(equalizedExpression));
    }

}
