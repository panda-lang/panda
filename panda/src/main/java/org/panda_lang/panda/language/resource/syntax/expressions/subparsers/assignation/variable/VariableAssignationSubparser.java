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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.LocalChannel;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.language.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.language.interpreter.parser.pipeline.Handler;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.language.architecture.dynamic.assigner.AssignerExpression;
import org.panda_lang.language.architecture.type.ConstructorScope;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.AssignationSubparserBootstrap;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class VariableAssignationSubparser extends AssignationSubparserBootstrap {

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(PandaPipeline.ASSIGNER);
    }

    @Override
    public double priority() {
        return AssignationPriorities.VARIABLE_ASSIGNATION;
    }

    @Override
    protected BootstrapInitializer<@Nullable ExpressionResult> initialize(Context context, BootstrapInitializer<@Nullable ExpressionResult> initializer) {
        return initializer;
    }

    @Override
    protected Object customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        ExpressionParser parser = context.getComponent(Components.EXPRESSION);

        try {
            SourceStream stream = new PandaSourceStream(source);
            ExpressionTransaction transaction = parser.parse(context, stream);

            if (stream.hasUnreadSource()) {
                transaction.rollback();
                return false;
            }

            if (!(transaction.getExpression() instanceof AccessorExpression)) {
                throw new PandaParserFailure(context, source, "Expression is not accessor");
            }

            channel.allocated("accessor", transaction.getExpression());
            return true;
        } catch (Exception e) {
            return e;
        }
    }

    @Autowired(order = 1)
    public ExpressionResult parse(LocalChannel channel, @Ctx Scope block, @Ctx Expression expression, @Channel Location location) {
        Accessor<?> accessor = channel.get("accessor", AccessorExpression.class).getAccessor();
        boolean initialization = block.getFramedScope() instanceof ConstructorScope;
        Assigner<?> assigner = accessor.toAssigner(location, initialization, expression);

        if (initialization) {
            assigner.getAccessor().getVariable().initialize();
        }

        return ExpressionResult.of(new AssignerExpression(assigner));
    }

}
