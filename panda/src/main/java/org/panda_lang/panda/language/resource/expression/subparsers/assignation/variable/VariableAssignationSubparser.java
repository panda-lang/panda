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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.language.architecture.dynamic.assigner.AssignerExpression;
import org.panda_lang.panda.language.architecture.prototype.standard.constructor.ConstructorFrame;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.language.resource.expression.subparsers.assignation.AssignationSubparserBootstrap;

@Registrable(pipeline = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_ASSIGNATION)
public final class VariableAssignationSubparser extends AssignationSubparserBootstrap {

    @Override
    protected BootstrapInitializer<@Nullable ExpressionResult> initialize(Context context, BootstrapInitializer<@Nullable ExpressionResult> initializer) {
        return initializer;
    }

    @Override
    protected Object customHandle(ParserHandler handler, Context context, Channel channel, Snippet source) {
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);

        try {
            SourceStream stream = new PandaSourceStream(source);
            ExpressionTransaction transaction = parser.parse(context, stream);

            if (stream.hasUnreadSource()) {
                transaction.rollback();
                return false;
            }

            if (!(transaction.getExpression() instanceof AccessorExpression)) {
                return PandaParserFailure.builder("Expression is not accessor", context)
                        .withStreamOrigin(source)
                        .build();
            }

            channel.put("accessor", transaction.getExpression());
            return true;
        } catch (Exception e) {
            return e;
        }
    }

    @Autowired
    ExpressionResult parse(@Component Channel channel, @Component Scope scope, @Component Expression expression) {
        Accessor<?> accessor = channel.get("accessor", AccessorExpression.class).getAccessor();
        Assigner<?> assigner = accessor.toAssigner(scope.getFrame() instanceof ConstructorFrame, expression);

        return ExpressionResult.of(new AssignerExpression(assigner));
    }

}
