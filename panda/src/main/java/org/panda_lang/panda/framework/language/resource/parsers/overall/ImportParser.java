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

package org.panda_lang.panda.framework.language.resource.parsers.overall;

import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.Registrable;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.utilities.commons.ClassUtils;

import java.util.Optional;

@Registrable(pipeline = UniversalPipelines.HEAD_LABEL)
public final class ImportParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.IMPORT))
                .pattern("import <class:condition token {type:unknown}, token {value:_}, token {value:.}>");
    }

    @Autowired
    void parseImport(Context context, @Component PandaScript script, @Component ModuleLoader loader, @Src("class") Snippet clazzSource) {
        Optional<Class<?>> importedClass = ClassUtils.forName(clazzSource.asString());

        if (!importedClass.isPresent()) {
            throw PandaParserFailure.builder("Class " + clazzSource.asString() + " does not exist", context)
                    .withStreamOrigin(clazzSource)
                    .build();
        }

        Class<?> clazz = importedClass.get();
        ClassPrototypeGeneratorManager.getInstance().generate(loader.getLocalModule(), clazz, clazz.getSimpleName());
    }

}
