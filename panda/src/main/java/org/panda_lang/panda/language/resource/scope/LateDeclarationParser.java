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

package org.panda_lang.panda.language.resource.scope;

import org.panda_lang.framework.design.architecture.dynamic.Scope;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.panda.language.architecture.statement.VariableConstants;
import org.panda_lang.panda.language.architecture.statement.VariableDataInitializer;
import org.panda_lang.panda.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = UniversalPipelines.SCOPE_LABEL, priority = PandaPriorities.CONTAINER_LATE_DECLARATION)
public final class LateDeclarationParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LATE))
                .pattern("late " + VariableConstants.DECLARATION);
    }

    @Autowired
    void parse(Context context, @Inter ExtractorResult result, @Component Scope parent, @Src("type") Snippetable type, @Src("name") Snippetable name) {
        boolean mutable = result.hasIdentifier(Keywords.MUT.getValue());
        boolean nillable = result.hasIdentifier(Keywords.NIL.getValue());

        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, parent);
        VariableData variableData = dataInitializer.createVariableData(type, name, mutable, nillable);
        parent.createVariable(variableData);
    }

}
