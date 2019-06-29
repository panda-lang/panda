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

package org.panda_lang.panda.framework.language.resource.parsers;

import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.PandaApplication;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.Resources;
import org.panda_lang.panda.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaContext;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserDebug;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationCycles;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.PandaGeneration;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSourceSet;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

public class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(Source source) {
        PandaApplication application = new PandaApplication(interpretation.getEnvironment());

        SourceSet sources = new PandaSourceSet();
        sources.addSource(source);

        Environment environment = interpretation.getEnvironment();
        Resources resources = environment.getResources();

        ModuleLoader loader = new PandaModuleLoader(environment.getModulePath())
                .include(environment.getModulePath().getDefaultModule())
                .include(environment.getModulePath(), "panda-lang");

        PandaGeneration generation = new PandaGeneration();
        generation.initialize(GenerationCycles.getValues());

        Context context = new PandaContext()
                .withComponent(UniversalComponents.APPLICATION, application)
                .withComponent(UniversalComponents.ENVIRONMENT, environment)
                .withComponent(UniversalComponents.INTERPRETATION, interpretation)
                .withComponent(UniversalComponents.GENERATION, generation)
                .withComponent(UniversalComponents.MODULE_LOADER, loader)
                .withComponent(UniversalComponents.PIPELINE, resources.getPipelinePath())
                .withComponent(UniversalComponents.PARSER_DEBUG, new PandaParserDebug(true))
                .withComponent(UniversalComponents.EXPRESSION, resources.getExpressionSubparsers().toExpressionParser())
                .withComponent(UniversalComponents.SOURCES, sources);

        Lexer lexer = PandaLexer.of(interpretation.getLanguage().getSyntax())
                .enableSections()
                .build();

        for (Source current : sources) {
            PandaScript pandaScript = new PandaScript(current.getTitle());
            application.addScript(pandaScript);

            interpretation.execute(() -> {
                Snippet snippet = lexer.convert(current);
                SourceStream sourceStream = new PandaSourceStream(snippet);

                Context delegatedContext = context.fork()
                        .withComponent(UniversalComponents.SOURCE, snippet)
                        .withComponent(UniversalComponents.SOURCE_STREAM, sourceStream)
                        .withComponent(UniversalComponents.SCRIPT, pandaScript)
                        .withComponent(PandaComponents.PANDA_SCRIPT, pandaScript);

                OverallParser overallParser = new OverallParser(delegatedContext);

                while (interpretation.isHealthy() && overallParser.hasNext()) {
                    interpretation.execute(() -> overallParser.parseNext(delegatedContext));
                }
            });
        }

        return interpretation
                .execute(generation::launch)
                .execute(() -> application);
    }

}
