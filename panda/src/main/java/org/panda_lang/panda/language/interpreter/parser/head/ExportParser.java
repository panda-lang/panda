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

package org.panda_lang.panda.language.interpreter.parser.head;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ImportElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.panda.language.interpreter.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.bootstraps.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class ExportParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.EXPORT))
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        KeywordElement.create(Keywords.EXPORT),
                        ImportElement.create("class").javaClass()
                ));
    }

    @Autowired
    void parseImport(Context context, @Component PandaScript script, @Src("class") Snippet className) {
        script.getModule().add(ConveyanceUtils.fetchPrototype(context, className));
    }

}
