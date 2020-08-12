/*
 * Copyright (c) 2020 Dzikoysk
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

import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.interpreter.Interpretation;
import org.panda_lang.language.interpreter.lexer.Lexer;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.language.interpreter.source.Source;
import org.panda_lang.language.interpreter.source.SourceSet;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.resource.Resources;
import org.panda_lang.language.architecture.module.PandaImports;
import org.panda_lang.language.architecture.type.generator.TypeGeneratorManager;
import org.panda_lang.language.interpreter.lexer.PandaLexer;
import org.panda_lang.language.interpreter.parser.PandaContext;
import org.panda_lang.language.interpreter.parser.stage.Stages;
import org.panda_lang.language.interpreter.parser.stage.PandaStageController;
import org.panda_lang.language.interpreter.parser.pipeline.PandaLocalChannel;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineParser;
import org.panda_lang.language.interpreter.source.PandaSourceSet;
import org.panda_lang.language.interpreter.token.PandaSourceStream;
import org.panda_lang.panda.language.architecture.PandaApplication;
import org.panda_lang.panda.language.architecture.PandaScript;

public final class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(Source source) {
        TypeGeneratorManager.getInstance().disposeCache();

        Environment environment = interpretation.getInterpreter().getEnvironment();
        Resources resources = environment.getController().getResources();
        PandaApplication application = new PandaApplication(environment);

        PandaStageController generation = new PandaStageController();
        generation.initialize(Stages.getValues());

        SourceSet sources = new PandaSourceSet();
        sources.addSource(source);

        Lexer lexer = PandaLexer.of(environment.getController().getLanguage().getSyntax())
                .enableSections()
                .build();

        Context context = new PandaContext()
                .withComponent(Components.APPLICATION, application)
                .withComponent(Components.ENVIRONMENT, environment)
                .withComponent(Components.INTERPRETATION, interpretation)
                .withComponent(Components.GENERATION, generation)
                .withComponent(Components.TYPE_LOADER, environment.getTypeLoader())
                .withComponent(Components.PIPELINE, resources.getPipelinePath())
                .withComponent(Components.EXPRESSION, resources.getExpressionSubparsers().toParser())
                .withComponent(Components.SOURCES, sources);

        PipelineParser<?> headParser = new PipelineParser<>(Pipelines.HEAD);

        for (Source current : sources) {
            PandaScript script = new PandaScript(current.getId());
            application.addScript(script);

            interpretation.execute(() -> {
                Snippet snippet = lexer.convert(current);
                SourceStream stream = new PandaSourceStream(snippet);

                Imports imports = new PandaImports(environment.getModulePath(), environment.getTypeLoader());
                imports.importModule("java");
                imports.importModule("panda");

                Context delegatedContext = context.fork()
                        .withComponent(PandaComponents.PANDA_SCRIPT, script)
                        .withComponent(Components.SCRIPT, script)
                        .withComponent(Components.IMPORTS, imports)
                        .withComponent(Components.SOURCE, snippet)
                        .withComponent(Components.STREAM, stream)
                        .withComponent(Components.CURRENT_SOURCE, snippet)
                        .withComponent(Components.CHANNEL, new PandaLocalChannel());

                interpretation.execute(() -> headParser.parse(delegatedContext, stream));
            });
        }

        return interpretation
                .execute(generation::launch)
                .execute(() -> application);
    }

}
