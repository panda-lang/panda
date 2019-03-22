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

import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.PandaApplication;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.PandaModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorWorker;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsers;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsersLoaderOld;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.messenger.translators.exception.ExceptionTranslator;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserDebug;
import org.panda_lang.panda.framework.language.interpreter.parser.defaults.OverallParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.GenerationTypes;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.PandaGeneration;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.parsers.overall.CommentParser;
import org.panda_lang.panda.utilities.commons.TimeUtils;

import java.util.Collections;

public class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(SourceSet sourceSet) {
        PandaApplication application = new PandaApplication();
        Environment environment = interpretation.getEnvironment();

        ModuleLoader loader = new PandaModuleLoader(environment.getModulePath());
        loader.include(environment.getModulePath().getDefaultModule());

        PandaGeneration generation = new PandaGeneration();
        generation.initialize(GenerationTypes.getValues());

        PandaParserDebug debug = new PandaParserDebug(true);
        debug.enableTailing(true);

        ParserData baseData = new PandaParserData();
        baseData.setComponent(UniversalComponents.APPLICATION, application);
        baseData.setComponent(UniversalComponents.ENVIRONMENT, environment);
        baseData.setComponent(UniversalComponents.INTERPRETATION, interpretation);
        baseData.setComponent(UniversalComponents.PIPELINE, environment.getPipelinePath());
        baseData.setComponent(UniversalComponents.GENERATION, generation);
        baseData.setComponent(UniversalComponents.MODULE_LOADER, loader);
        baseData.setComponent(UniversalComponents.PARSER_DEBUG, debug);

        ExceptionTranslator exceptionTranslator = new ExceptionTranslator(interpretation);
        interpretation.getMessenger().addMessageTranslator(exceptionTranslator);

        ExpressionSubparsers subparsers = new ExpressionSubparsers(Collections.emptyList());
        ExpressionParserOld expressionParser = new ExpressionParserOld(null, subparsers);
        baseData.setComponent(PandaComponents.EXPRESSION, expressionParser);

        ExpressionSubparsersLoaderOld subparsersLoader = new ExpressionSubparsersLoaderOld();
        subparsers.merge(interpretation.execute(() -> subparsersLoader.load(baseData)));
        subparsers.getSubparsers().forEach(element -> element.afterInitialization(expressionParser));

        for (Source source : sourceSet.getSources()) {
            PandaScript pandaScript = new PandaScript(source.getTitle());
            exceptionTranslator.updateLocation(source.getTitle());

            interpretation.execute(() -> {
                PandaLexer lexer = PandaLexer.of(interpretation.getLanguage().getSyntax(), source).build();
                Snippet snippet = CommentParser.uncomment(lexer.convert());

                PandaSourceStream sourceStream = new PandaSourceStream(snippet);
                exceptionTranslator.updateSource(sourceStream);

                ParserData delegatedData = baseData.fork();
                delegatedData.setComponent(UniversalComponents.SOURCE, snippet);
                delegatedData.setComponent(UniversalComponents.SOURCE_STREAM, sourceStream);
                delegatedData.setComponent(UniversalComponents.SCRIPT, pandaScript);
                delegatedData.setComponent(PandaComponents.PANDA_SCRIPT, pandaScript);

                OverallParser overallParser = new OverallParser(delegatedData);
                application.addScript(pandaScript);

                while (interpretation.isHealthy() && overallParser.hasNext()) {
                    interpretation.execute(() -> overallParser.parseNext(delegatedData));
                }

                // throw new RuntimeException("ฅ^•ﻌ•^ฅ");
            });
        }

        interpretation
                .execute(() -> generation.execute(baseData))
                .execute(() -> application);

        PandaFramework.getLogger().debug("");
        PandaFramework.getLogger().debug("--- Parse details ");

        PandaFramework.getLogger().debug("• Expressions Time: " + TimeUtils.toMilliseconds(ExpressionParserOld.fullTime));
        PandaFramework.getLogger().debug("• Token Pattern Time: " + TimeUtils.toMilliseconds(ExtractorWorker.fullTime));

        PandaFramework.getLogger().debug("• Total Native Load Time: " + TimeUtils.toMilliseconds(ClassPrototypeGeneratorManager.getTotalLoadTime()));
        PandaFramework.getLogger().debug("• Total Handle Time: " + TimeUtils.toMilliseconds(environment.getPipelinePath().getTotalHandleTime()));

        PandaFramework.getLogger().debug("• Amount of references: " + environment.getModulePath().getAmountOfReferences());
        PandaFramework.getLogger().debug("• Amount of used prototypes: " + environment.getModulePath().getAmountOfUsedPrototypes());

        return application;
    }

}
