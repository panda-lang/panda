/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.statement.PandaBlock;
import org.panda_lang.framework.language.architecture.statement.PandaVariableDataInitializer;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Local;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalData;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.LinearPatternInterceptor;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class TryCatchParser extends ParserBootstrap<Void> {

    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.TRY))
                .interceptor(new LinearPatternInterceptor())
                .pattern("try try-body:{~} catch catch-what:(~) catch-body:{~}");
    }

    @Autowired
    void parse(Context context, LocalData data, @Component Scope parent, @Inter SourceLocation location, @Src("try-body") Snippet tryBody) throws Exception {
        Scope tryBlock = SCOPE_PARSER.parse(context, new PandaBlock(parent, location), tryBody);
        TryCatch tryCatch = data.allocated(new TryCatch(location, tryBlock, new PandaBlock(parent, location)));
        parent.addStatement(tryCatch);
    }

    @Autowired(order = 1)
    void parse(Context context, @Component Scope parent, @Local TryCatch tryCatch, @Src("catch-what") Snippet catchWhat, @Src("catch-body") Snippet catchBody) throws Exception {
        Scope catchBlock = new PandaBlock(parent, catchWhat.getLocation());

        PandaVariableDataInitializer dataInitializer = new PandaVariableDataInitializer(context, catchBlock);
        VariableData variableData = dataInitializer.createVariableData(catchWhat, false, false);
        Variable variable = catchBlock.createVariable(variableData);

        SCOPE_PARSER.parse(context, catchBlock, catchBody);
        DynamicClass type = variableData.getType().getAssociatedClass();

        if (type.isAssignableTo(Throwable.class)) {
            //noinspection unchecked
            tryCatch.addHandler((Class<? extends Throwable>) type.fetchImplementation(), variable, catchBlock);
        }
    }

}
