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

package org.panda_lang.panda.language.interpreter.parser;

import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.interpreter.Interpretation;
import org.panda_lang.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.design.interpreter.source.SourceSet;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.design.resource.Resources;
import org.panda_lang.framework.language.architecture.module.PandaImports;
import org.panda_lang.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.framework.language.interpreter.parser.generation.PandaGeneration;
import org.panda_lang.framework.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.framework.language.interpreter.source.PandaSourceSet;
import org.panda_lang.framework.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.architecture.PandaApplication;
import org.panda_lang.panda.language.architecture.PandaScript;

public final class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(Source source) {
        Environment environment = interpretation.getEnvironment();
        Resources resources = environment.getResources();

        PandaApplication application = new PandaApplication(environment);
        ModuleLoader loader = new PandaModuleLoader(environment.getModulePath());

        PandaGeneration generation = new PandaGeneration();
        generation.initialize(GenerationCycles.getValues());

        SourceSet sources = new PandaSourceSet();
        sources.addSource(source);

        Lexer lexer = PandaLexer.of(interpretation.getLanguage().getSyntax())
                .enableSections()
                .build();

        Context context = new PandaContext()
                .withComponent(Components.APPLICATION, application)
                .withComponent(Components.ENVIRONMENT, environment)
                .withComponent(Components.INTERPRETATION, interpretation)
                .withComponent(Components.GENERATION, generation)
                .withComponent(Components.MODULE_LOADER, loader)
                .withComponent(Components.PIPELINE, resources.getPipelinePath())
                .withComponent(Components.EXPRESSION, resources.getExpressionSubparsers().toParser())
                .withComponent(Components.SOURCES, sources);

        for (Source current : sources) {
            PandaScript script = new PandaScript(current.getId(), new PandaModuleLoader(loader));
            application.addScript(script);

            interpretation.execute(() -> {
                Snippet snippet = lexer.convert(current);
                SourceStream sourceStream = new PandaSourceStream(snippet);

                Imports imports = new PandaImports(loader);
                imports.importModule("java");
                imports.importModule("panda");

                Context delegatedContext = context.fork()
                        .withComponent(Components.MODULE_LOADER, script.getModuleLoader())
                        .withComponent(Components.SCRIPT, script)
                        .withComponent(PandaComponents.PANDA_SCRIPT, script)
                        .withComponent(Components.IMPORTS, imports)
                        .withComponent(Components.SOURCE, snippet)
                        .withComponent(Components.STREAM, sourceStream)
                        .withComponent(Components.CURRENT_SOURCE, snippet);

                PipelineParser<?> parser = new PipelineParser<>(Pipelines.HEAD, delegatedContext);
                interpretation.execute(() -> parser.parse(delegatedContext, true));
            });
        }

        return interpretation
                .execute(generation::launch)
                .execute(() -> application);
    }

}
