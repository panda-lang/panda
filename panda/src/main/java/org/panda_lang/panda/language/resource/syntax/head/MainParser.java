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

package org.panda_lang.panda.language.resource.syntax.head;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Int;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Cache;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.Delegation;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalCache;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.LinearPatternInterceptor;

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class MainParser extends ParserBootstrap<Void> {

    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.MAIN))
                .interceptor(new LinearPatternInterceptor())
                .pattern("main body:{~}");
    }

    @Autowired(order = 1, delegation = Delegation.NEXT_DEFAULT)
    void createScope(LocalCache cache, @Ctx Script script, @Int Location location) {
        script.addStatement(cache.allocated(new MainScope(location)));
    }

    @Autowired(order = 2, delegation = Delegation.NEXT_AFTER)
    void parseScope(Context context, @Cache MainScope main, @Src("body") @Nullable Snippet body) throws Exception {
        SCOPE_PARSER.parse(context.fork(), main, body);
    }

}
