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

package org.panda_lang.panda.framework.language.resource.parsers.container.assignation.subparsers.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.Assigner;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationSubparserBootstrap;

@ParserRegistration(target = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_ASSIGNATION)
public class VariableAssignationSubparser extends AssignationSubparserBootstrap {

    @Override
    protected BootstrapParserBuilder<@Nullable Statement> initialize(ParserData data, BootstrapParserBuilder<@Nullable Statement> defaultBuilder) {
        return defaultBuilder.pattern("<source:reader expression include field>");
    }

    @Autowired
    protected Statement parse(@Src("source") AccessorExpression source, @Component Expression expression) {
        Accessor<?> accessor = source.getAccessor();
        Assigner<?> assigner = accessor.toAssigner(expression);

        return assigner.toExecutableStatement();
    }

}
