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

package org.panda_lang.panda.framework.language.resource.expression.subparsers.assignation.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.design.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.design.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.expression.subparsers.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.expression.subparsers.assignation.AssignationSubparserBootstrap;

@Registrable(pipeline = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_ASSIGNATION)
public class VariableAssignationSubparser extends AssignationSubparserBootstrap {

    private AccessorExpression accessorExpression;

    @Override
    protected BootstrapInitializer<@Nullable Assigner<?>> initialize(Context context, BootstrapInitializer<@Nullable Assigner<?>> initializer) {
        return initializer;
    }

    @Override
    protected Object customHandle(ParserHandler handler, Context context, Snippet source) {
        ExpressionParser parser = context.getComponent(UniversalComponents.EXPRESSION);

        try {
            SourceStream stream = new PandaSourceStream(source);
            Expression expression = parser.parse(context, stream);

            if (stream.hasUnreadSource()) {
                return false;
            }

            if (!(expression instanceof AccessorExpression)) {
                return PandaParserFailure.builder("Expression is not accessor", context)
                        .withStreamOrigin(source)
                        .build();
            }

            accessorExpression = (AccessorExpression) expression;
            return true;
        } catch (Exception e) {
            return e;
        }
    }

    @Autowired
    protected Assigner<?> parse(@Component Expression expression) {
        Accessor<?> accessor = accessorExpression.getAccessor();
        return accessor.toAssigner(expression);
    }

}
