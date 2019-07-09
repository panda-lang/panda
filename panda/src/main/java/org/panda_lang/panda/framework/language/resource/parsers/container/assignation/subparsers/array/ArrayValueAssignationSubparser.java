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

package org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.EmptyHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptors.EmptyInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.auxiliary.Section;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

@ParserRegistration(pipeline = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_DECLARATION)
public class ArrayValueAssignationSubparser extends AssignationSubparserBootstrap {

    private static final ArrayValueAccessorParser PARSER = new ArrayValueAccessorParser();

    @Override
    protected BootstrapInitializer<@Nullable Statement> initialize(Context context, BootstrapInitializer<@Nullable Statement> initializer) {
        return initializer
                .interceptor(new EmptyInterceptor())
                .handler(new EmptyHandler());
    }

    @Override
    public Boolean customHandle(ParserHandler handler, Context context, Snippet source) {
        TokenRepresentation sectionRepresentation = source.getLast();

        if (sectionRepresentation.getType() != TokenType.SECTION) {
            return false;
        }

        Section section = sectionRepresentation.toToken();
        return section.getSeparator().equals(Separators.SQUARE_BRACKET_LEFT);
    }

    @Autowired
    Statement parse(Context context, @Component SourceStream source, @Component(AssignationComponents.EXPRESSION_LABEL) Expression expression) {
        return PARSER.parse(context, source.toSnippet(), new ArrayValueAccessor.ArrayValueAccessorAction() {
            @Override
            public @Nullable PandaStaticValue perform(Flow flow, ArrayClassPrototype prototype, ClassPrototype type, Object[] array, int index) {
                array[index] = expression.evaluate(flow).getObject();
                return null;
            }

            @Override
            public ClassPrototype getType() {
                return PandaTypes.VOID;
            }
        });
    }

}
