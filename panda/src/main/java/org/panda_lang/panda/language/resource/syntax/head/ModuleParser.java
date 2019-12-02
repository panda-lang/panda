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

package org.panda_lang.panda.language.resource.syntax.head;

import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ImportElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.UnitElement;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.architecture.module.PandaModule;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.architecture.PandaScript;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

@RegistrableParser(pipeline = Pipelines.HEAD_LABEL)
public final class ModuleParser extends ParserBootstrap<Object> {

    @Override
    protected BootstrapInitializer<Object> initialize(Context context, BootstrapInitializer<Object> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.MODULE))
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        UnitElement.create("").content(Keywords.MODULE.getValue()),
                        ImportElement.create("module").pandaModule()
                ));
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(@Component Environment environment, @Component Imports imports, @Component PandaScript script, @Inter SourceLocation location, @Src("module") Snippet source) {
        if (script.select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        String moduleName = source.asSource();
        ModuleLoader moduleLoader = imports.getModuleLoader();

        Module module = environment.getModulePath().get(moduleName, moduleLoader).orElseGet(() -> {
            Module pandaModule = new PandaModule(moduleName, moduleLoader);
            environment.getModulePath().include(pandaModule);
            return pandaModule;
        });

        ModuleStatement moduleStatement = new ModuleStatement(location, module);
        script.addStatement(moduleStatement);
        script.setModule(module);
        imports.importModule(module);
    }

}
