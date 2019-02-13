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

package org.panda_lang.panda.framework.language.resource.parsers.expression.assignation.subparsers.variable;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.AutowiredParameters;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Type;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.VariableAssignerUtils;
import org.panda_lang.panda.framework.language.resource.parsers.expression.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.resource.parsers.expression.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.parsers.expression.assignation.AssignationSubparserBootstrap;

@ParserRegistration(target = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_DECLARATION)
public class VariableDeclarationSubparser extends AssignationSubparserBootstrap {

    private static final VariableInitializer INITIALIZER = new VariableInitializer();

    @Override
    public BootstrapParserBuilder<@Nullable Statement> initialize(ParserData data, BootstrapParserBuilder<@Nullable Statement> defaultBuilder) {
        return defaultBuilder.pattern(VariableInitializer.DECLARATION_PARSER);
    }

    @Autowired
    @AutowiredParameters(skip = 2, value = {
            @Type(with = Component.class),
            @Type(with = Component.class),
            @Type(with = Src.class, value = "type"),
            @Type(with = Src.class, value = "name"),
            @Type(with = Component.class, value = AssignationComponents.EXPRESSION_LABEL)
    })
    public @Nullable Statement parse(ParserData data, ExtractorResult result, ModuleLoader loader, Scope scope, Tokens type, Tokens name, Expression expression) {
        if (!result.isMatched()) {
            return null;
        }

        boolean mutable = result.hasIdentifier("mutable");
        boolean nullable = result.hasIdentifier("nullable");
        Variable variable = INITIALIZER.createVariable(data, loader, scope, mutable, nullable, type.asString(), name.asString());

        return VariableAssignerUtils.of(data, scope, variable, expression).toExecutableStatement();
    }

}
