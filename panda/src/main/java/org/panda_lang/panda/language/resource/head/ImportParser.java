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

package org.panda_lang.panda.language.resource.head;

import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.language.architecture.prototype.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.utilities.commons.ClassUtils;

import java.util.Optional;

@Registrable(pipeline = Pipelines.HEAD_LABEL)
public final class ImportParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.IMPORT))
                .pattern("import <class:condition token {type:unknown}, token {value:_}, token {value:.}>");
    }

    @Autowired
    void parseImport(Context context, @Component PandaScript script, @Component ModuleLoader loader, @Src("class") Snippet clazzSource) {
        Optional<Class<?>> importedClass = ClassUtils.forName(clazzSource.asSource());

        if (!importedClass.isPresent()) {
            throw PandaParserFailure.builder("Class " + clazzSource.asSource() + " does not exist", context)
                    .withStreamOrigin(clazzSource)
                    .build();
        }

        Class<?> clazz = importedClass.get();
        ClassPrototypeGeneratorManager.getInstance().generate(loader.getLocalModule(), clazz, clazz.getSimpleName());
    }

}
