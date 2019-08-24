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

package org.panda_lang.panda.framework.language.resource.scope.branching;

import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.BootstrapComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = UniversalPipelines.SCOPE_LABEL)
public class BreakParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.BREAK))
                .interceptor(new LinearPatternInterceptor())
                .pattern("break");
    }

    @Autowired
    void parseBreak(@Component(BootstrapComponents.CURRENT_SOURCE_LABEL) Snippet source, @Component Scope scope) {
        BranchingUtils.parseBranchingStatement(source, scope, Break::new);
    }

}
