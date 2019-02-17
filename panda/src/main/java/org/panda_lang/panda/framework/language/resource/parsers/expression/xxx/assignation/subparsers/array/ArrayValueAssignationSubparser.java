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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.subparsers.array;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.EmptyHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.interceptor.EmptyInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.PandaTypes;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.separator.Separators;

@ParserRegistration(target = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_DECLARATION)
public class ArrayValueAssignationSubparser extends AssignationSubparserBootstrap {

    private static final ArrayValueAccessorParser PARSER = new ArrayValueAccessorParser();

    @Override
    protected BootstrapParserBuilder<@Nullable Statement> initialize(ParserData data, BootstrapParserBuilder<@Nullable Statement> defaultBuilder) {
        return defaultBuilder
                .interceptor(new EmptyInterceptor())
                .handler(new EmptyHandler());
    }

    @Override
    public boolean customHandle(ParserHandler handler, ParserData data, SourceStream source) {
        if (!source.toTokenizedSource().getLast().contentEquals(Separators.SQUARE_BRACKET_RIGHT)) {
            return false;
        }

        source.read(source.getUnreadLength());
        return true;
    }

    @Autowired
    public Statement parse(ParserData data, @Component SourceStream source, @Component(AssignationComponents.EXPRESSION_LABEL) Expression expression) {
        return PARSER.parse(data, source.toTokenizedSource(), new ArrayValueAccessor.ArrayValueAccessorAction() {
            @Override
            public @Nullable PandaValue perform(ExecutableBranch branch, ArrayClassPrototype prototype, ClassPrototype type, Object[] array, Number index) {
                array[index.intValue()] = expression.getExpressionValue(branch).getObject();
                return null;
            }

            @Override
            public ClassPrototype getType() {
                return PandaTypes.VOID;
            }
        });
    }

}
