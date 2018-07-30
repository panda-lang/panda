/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.parsers.defaults;

import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeGeneratorManager;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.*;
import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.framework.design.interpreter.source.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.*;
import org.panda_lang.panda.framework.design.architecture.PandaApplication;
import org.panda_lang.panda.framework.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.lexer.*;
import org.panda_lang.panda.framework.language.interpreter.messenger.translators.exception.*;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.defaults.*;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.PandaCasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.parsers.general.comment.*;
import org.panda_lang.panda.utilities.commons.objects.*;

public class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(SourceSet sourceSet) {
        PandaApplication application = new PandaApplication();

        Environment environment = interpretation.getEnvironment();
        ModulePath registry = environment.getModulePath();
        Module defaultModule = registry.get((String) null);

        Language elements = interpretation.getLanguage();
        PipelineRegistry pipelineRegistry = elements.getParserPipelineRegistry();
        CasualParserGeneration generation = new PandaCasualParserGeneration();

        ParserData baseData = new PandaParserData();
        baseData.setComponent(UniversalComponents.INTERPRETATION, interpretation);
        baseData.setComponent(UniversalComponents.PIPELINE, pipelineRegistry);
        baseData.setComponent(UniversalComponents.GENERATION, generation);
        baseData.setComponent(PandaComponents.MODULE_REGISTRY, registry);

        ExceptionTranslator exceptionTranslator = new ExceptionTranslator(interpretation);
        interpretation.getMessenger().addMessageTranslator(exceptionTranslator);

        for (Source source : sourceSet.getSources()) {
            PandaScript pandaScript = new PandaScript(source.getTitle());
            exceptionTranslator.updateLocation(source.getTitle());

            interpretation.execute(() -> {
                pandaScript.getImportRegistry().include(defaultModule);

                PandaLexer lexer = new PandaLexer(elements.getSyntax(), source);
                TokenizedSource tokenizedSource = CommentAssistant.uncomment(lexer.convert());

                PandaSourceStream sourceStream = new PandaSourceStream(tokenizedSource);
                exceptionTranslator.updateSource(sourceStream);

                ParserData delegatedData = baseData.fork();
                delegatedData.setComponent(UniversalComponents.SOURCE, tokenizedSource);
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

            PandaFramework.getLogger().debug("Total Native Load Time: " + TimeUtils.toMilliseconds(ClassPrototypeGeneratorManager.getTotalLoadTime()));
            PandaFramework.getLogger().debug("Total Handle Time: " + TimeUtils.toMilliseconds(pipelineRegistry.getTotalHandleTime()));
        }

        return interpretation
                .execute(() -> generation.execute(baseData))
                .execute(() -> application);
    }

}
