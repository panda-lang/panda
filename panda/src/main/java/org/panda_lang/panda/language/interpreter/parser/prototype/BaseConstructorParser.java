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

package org.panda_lang.panda.language.interpreter.parser.prototype;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.prototype.BaseConstructor;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.ArgumentsParser;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL)
public final class BaseConstructorParser extends ParserBootstrap {

    private static final ArgumentsParser ARGUMENTS_PARSER = new ArgumentsParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.BASE))
                .interceptor(new LinearPatternInterceptor())
                .pattern("base arguments:(~)");
    }

    @Autowired
    void parse(Context context, @Component Scope parent, @Inter SourceLocation location, @Src("arguments") Snippet arguments) {
        parent.addStatement(new BaseConstructor(location, null, ARGUMENTS_PARSER.parse(context, arguments)));
    }

}
