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
import org.panda_lang.panda.framework.design.interpreter.messenger.translator.PandaTranslatorLayoutManager;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.Resources;
import org.panda_lang.panda.framework.language.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.messenger.layouts.ExceptionTranslatorLayout;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserDebug;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationTypes;
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
        generation.initialize(GenerationTypes.getValues());

        ParserData baseData = new PandaParserData()
                .setComponent(UniversalComponents.APPLICATION, application)
                .setComponent(UniversalComponents.ENVIRONMENT, environment)
                .setComponent(UniversalComponents.INTERPRETATION, interpretation)
                .setComponent(UniversalComponents.GENERATION, generation)
                .setComponent(UniversalComponents.MODULE_LOADER, loader)
                .setComponent(UniversalComponents.PIPELINE, resources.getPipelinePath())
                .setComponent(UniversalComponents.PARSER_DEBUG, new PandaParserDebug(true))
                .setComponent(UniversalComponents.EXPRESSION, resources.getExpressionSubparsers().toExpressionParser())
                .setComponent(UniversalComponents.SOURCES, sources);

        PandaTranslatorLayoutManager translatorLayoutManager = new PandaTranslatorLayoutManager(interpretation.getMessenger());
        ExceptionTranslatorLayout exceptionTranslatorLayout = translatorLayoutManager.load(new ExceptionTranslatorLayout());

        Lexer lexer = PandaLexer.of(interpretation.getLanguage().getSyntax())
                .enableSections()
                .build();

        for (Source current : sources) {
            PandaScript pandaScript = new PandaScript(current.getTitle());
            application.addScript(pandaScript);

            interpretation.execute(() -> {
                Snippet snippet = lexer.convert(current);
                SourceStream sourceStream = new PandaSourceStream(snippet);

                ParserData delegatedData = baseData.fork()
                        .setComponent(UniversalComponents.SOURCE, snippet)
                        .setComponent(UniversalComponents.SOURCE_STREAM, sourceStream)
                        .setComponent(UniversalComponents.SCRIPT, pandaScript)
                        .setComponent(PandaComponents.PANDA_SCRIPT, pandaScript);

                OverallParser overallParser = new OverallParser(delegatedData);
                exceptionTranslatorLayout.update(current.getTitle(), sourceStream);

                while (interpretation.isHealthy() && overallParser.hasNext()) {
                    interpretation.execute(() -> overallParser.parseNext(delegatedData));
                }
            });
        }

        return interpretation
                .execute(() -> generation.execute(baseData))
                .execute(() -> application);
    }

}
