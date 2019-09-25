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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.source.PandaURLSource;
import org.panda_lang.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

@Registrable(pipeline = Pipelines.HEAD_LABEL)
public final class RequireParser extends ParserBootstrap {

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.REQUIRE))
                .pattern("require (<require:condition token {type:unknown}, token {value:-}, token {value:.}>|<requiredFile>)");
    }

    @Autowired(cycle = GenerationCycles.TYPES_LABEL)
    void parse(Context context, @Component Imports imports, @Src("require") @Nullable Snippet require, @Src("requiredFile") @Nullable Snippet requiredFile) {
        if (require != null) {
            parseModule(context, imports, require);
            return;
        }

        parseFile(context, imports, Objects.requireNonNull(requiredFile));
    }

    private void parseModule(Context context, Imports imports, Snippet require) {
        Environment environment = context.getComponent(Components.ENVIRONMENT);

        String moduleName = require.asSource();
        Optional<Module> module = environment.getModulePath().get(moduleName);

        if (!module.isPresent()) {
            throw new PandaParserFailure(context, require, "Unknown module " + moduleName, "Make sure that the name does not have a typo and module is added to the module path");
        }

        ModuleLoader loader = context.getComponent(Components.MODULE_LOADER);
        loader.load(module.get());
    }

    private void parseFile(Context context, Imports imports, Snippet requiredFile) {
        TokenRepresentation fileToken = requiredFile.getFirst();

        if (!TokenUtils.hasName(fileToken, "String")) {
            throw new PandaParserFailure(context, fileToken, "Invalid token", "You should use string sequence to import file");
        }

        File file = new File(context.getComponent(Components.ENVIRONMENT).getDirectory(), fileToken.getValue() + ".panda");

        if (!file.exists()) {
            throw new PandaParserFailure(context, fileToken, "File " + file + " does not exist", "Make sure that the path does not have a typo");
        }

        context.getComponent(Components.SOURCES).addSource(PandaURLSource.fromFile(file));
    }

}
