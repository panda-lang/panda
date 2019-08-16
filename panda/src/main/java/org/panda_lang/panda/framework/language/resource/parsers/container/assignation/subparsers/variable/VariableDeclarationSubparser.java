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
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Inter;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.Registrable;
import org.panda_lang.panda.framework.language.architecture.dynamic.assigner.VariableAssignerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationComponents;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationPriorities;
import org.panda_lang.panda.framework.language.resource.parsers.container.assignation.AssignationSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = PandaPipelines.ASSIGNER_LABEL, priority = AssignationPriorities.VARIABLE_DECLARATION)
public class VariableDeclarationSubparser extends AssignationSubparserBootstrap {

    private static final VariableParser VARIABLE_PARSER = new VariableParser();

    @Override
    public BootstrapInitializer<@Nullable Statement> initialize(Context context, BootstrapInitializer<@Nullable Statement> initializer) {
        return initializer.pattern(VariableParser.DECLARATION);
    }

    @Autowired
    public @Nullable Statement parse(Context context, @Component Scope scope, @Inter ExtractorResult result, @Src("type") Snippet type, @Src("name") Snippet name) {
        if (!result.isMatched()) {
            return null;
        }

        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        Variable variable = VARIABLE_PARSER.createVariable(context, scope, mutable, nillable, type, name);

        return VariableAssignerUtils.of(context, scope, variable, context.getComponent(AssignationComponents.EXPRESSION)).toExecutableStatement();
    }

}
