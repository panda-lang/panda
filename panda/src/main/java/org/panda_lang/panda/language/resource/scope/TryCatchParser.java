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

import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.Variable;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.architecture.statement.PandaScope;
import org.panda_lang.panda.language.architecture.statement.VariableDataInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.language.resource.parsers.ScopeParser;
import org.panda_lang.panda.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = UniversalPipelines.SCOPE_LABEL)
public final class TryCatchParser extends ParserBootstrap {

    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.TRY))
                .pattern("try try-body:~{ catch catch-what:~( catch-body:~{");
    }

    @Autowired
    void parse(Context context, LocalData data, @Component Scope parent, @Src("try-body") Snippet tryBody) throws Exception {
        Scope tryScope = SCOPE_PARSER.parse(context, new PandaScope(parent), tryBody);
        TryCatch tryCatch = data.allocated(new TryCatch(tryScope, new PandaScope(parent)));
        parent.addStatement(tryCatch);
    }

    @Autowired(order = 1)
    void parse(Context context, @Component Scope parent, @Local TryCatch tryCatch, @Src("catch-what") Snippet catchWhat, @Src("catch-body") Snippet catchBody) throws Exception {
        Scope catchScope = new PandaScope(parent);

        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, catchScope);
        VariableData variableData = dataInitializer.createVariableData(catchWhat, false, false);
        Variable variable = catchScope.createVariable(variableData);

        SCOPE_PARSER.parse(context, catchScope, catchBody);
        Class<?> type = variableData.getType().getAssociatedClass();

        if (Throwable.class.isAssignableFrom(type)) {
            //noinspection unchecked
            tryCatch.addHandler((Class<? extends Throwable>) type, variable, catchScope);
        }
    }

}
