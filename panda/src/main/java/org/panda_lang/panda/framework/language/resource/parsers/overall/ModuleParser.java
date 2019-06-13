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
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.architecture.statement.ModuleStatement;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationTypes;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

import java.util.Optional;

@ParserRegistration(target = UniversalPipelines.OVERALL_LABEL)
public class ModuleParser extends UnifiedParserBootstrap {

    @Override
    protected BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.MODULE))
                .pattern("module <module:condition token {type:unknown}, token {value:-}, token {value:.}>[;]");
    }

    @Autowired(type = GenerationTypes.TYPES_LABEL)
    private void parse(ParserData data, @Component Environment environment, @Component ModuleLoader loader, @Component PandaScript script, @Src("module") Snippet moduleSource) {
        StringBuilder nameBuilder = new StringBuilder();

        for (TokenRepresentation representation : moduleSource.getTokensRepresentations()) {
            nameBuilder.append(representation.getValue());
        }

        String moduleName = nameBuilder.toString();

        if (!environment.getModulePath().hasModule(moduleName)) {
            environment.getModulePath().create(moduleName);
        }

        if (script.select(ModuleStatement.class).size() > 0) {
            throw new PandaParserException("Script contains more than one declaration of the group");
        }

        Optional<Module> module = environment.getModulePath().get(moduleName);

        if (!module.isPresent()) {
            throw new PandaParserException("Module '" + moduleName + "' does not exist");
        }

        ModuleStatement moduleStatement = new ModuleStatement(module.get());
        script.getStatements().add(moduleStatement);

        loader.include(moduleStatement.getModule());
        script.setModule(moduleStatement.getModule());
    }

}
