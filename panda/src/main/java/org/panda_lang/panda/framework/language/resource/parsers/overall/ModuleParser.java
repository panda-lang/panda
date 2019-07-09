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

import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.Module;
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
import org.panda_lang.panda.framework.language.architecture.module.PandaModule;
import org.panda_lang.panda.framework.language.architecture.statement.ModuleStatement;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = UniversalPipelines.HEAD_LABEL)
public final class ModuleParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.MODULE))
                .pattern("module <module:condition token {type:unknown}, token {value:-}, token {value:.}>");
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(@Component Environment environment, @Component ModuleLoader loader, @Component PandaScript script, @Src("module") Snippet moduleSource) {
        if (script.select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        String moduleName = moduleSource.asString();

        Module module = environment.getModulePath().get(moduleName).orElseGet(() -> {
            return environment.getModulePath().include(new PandaModule(moduleName));
        });

        ModuleStatement moduleStatement = new ModuleStatement(module);
        script.addStatement(moduleStatement);
        script.setModule(moduleStatement.getModule());

        loader.load(moduleStatement.getModule());
    }

}
