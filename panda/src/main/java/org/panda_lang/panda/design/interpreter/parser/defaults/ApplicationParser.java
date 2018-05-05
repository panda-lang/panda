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

package org.panda_lang.panda.design.interpreter.parser.defaults;

import org.panda_lang.panda.design.architecture.*;
import org.panda_lang.panda.design.interpreter.parser.*;
import org.panda_lang.panda.design.interpreter.parser.generation.*;
import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.framework.design.interpreter.source.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.*;
import org.panda_lang.panda.framework.language.interpreter.lexer.*;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.language.interpreter.parsers.*;
import org.panda_lang.panda.language.interpreter.parsers.general.comment.*;

public class ApplicationParser implements Parser {

    private final Interpretation interpretation;

    public ApplicationParser(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    public PandaApplication parse(SourceSet sourceSet) {
        PandaApplication application = new PandaApplication();

        Environment environment = interpretation.getEnvironment();
        ModuleRegistry registry = environment.getModuleRegistry();
        Module defaultModule = registry.getOrCreate(null);

        Language elements = interpretation.getLanguage();
        PipelineRegistry pipelineRegistry = elements.getParserPipelineRegistry();
        ParserPipeline overallPipeline = pipelineRegistry.getPipeline(PandaPipelines.OVERALL);

        CasualParserGeneration generation = new PandaCasualParserGeneration();
        CommentAssistant commentAssistant = new CommentAssistant();

        ParserData baseInfo = new PandaParserData();
        baseInfo.setComponent(UniversalComponents.INTERPRETATION, interpretation);
        baseInfo.setComponent(UniversalComponents.PIPELINE, pipelineRegistry);
        baseInfo.setComponent(UniversalComponents.GENERATION, generation);
        baseInfo.setComponent(PandaComponents.MODULE_REGISTRY, registry);

        for (Source source : sourceSet.getSources()) {
            PandaScript pandaScript = new PandaScript(source.getTitle());
            pandaScript.getImportRegistry().include(defaultModule);

            PandaLexer lexer = new PandaLexer(elements.getSyntax(), source.getContent());
            TokenizedSource tokenizedSource = lexer.convert();

            TokenizedSource uncommentedSource = commentAssistant.uncomment(tokenizedSource);
            PandaSourceStream sourceStream = new PandaSourceStream(uncommentedSource);

            ParserData delegatedInfo = baseInfo.fork();
            delegatedInfo.setComponent(UniversalComponents.SOURCE, uncommentedSource);
            delegatedInfo.setComponent(UniversalComponents.SOURCE_STREAM, sourceStream);
            delegatedInfo.setComponent(UniversalComponents.SCRIPT, pandaScript);
            delegatedInfo.setComponent(PandaComponents.PANDA_SCRIPT, pandaScript);

            OverallParser overallParser = new OverallParser(delegatedInfo);

            while (interpretation.isHealthy() && overallParser.hasNext()) {
                try {
                    overallParser.parseNext(delegatedInfo);
                } catch (Exception exception) {
                    interpretation.getMessenger().send(exception);
                }
            }

            if (!interpretation.isHealthy()) {
                break;
            }

            application.addScript(pandaScript);
        }

        if (!interpretation.isHealthy()) {
            return null;
        }

        generation.execute(baseInfo);

        /*
        for (Script script : application.getScripts()) {
            System.out.println(script.toString());
        }
        */

        if (!interpretation.isHealthy()) {
            return null;
        }

        return application;
    }

}
