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

import org.panda_lang.panda.design.architecture.PandaApplication;
import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.PandaInterpreter;
import org.panda_lang.panda.design.interpreter.parser.PandaParserInfo;
import org.panda_lang.panda.design.interpreter.parser.generation.PandaCasualParserGeneration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.language.elements.PandaElements;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.language.structure.general.comment.CommentAssistant;

public class ApplicationParser implements Parser {

    private final PandaInterpreter interpreter;

    public ApplicationParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    public PandaApplication parse(SourceSet sourceSet) {
        PandaApplication application = new PandaApplication();

        Environment environment = interpreter.getEnvironment();
        ModuleRegistry registry = environment.getModuleRegistry();
        Module defaultModule = registry.getOrCreate(null);

        PandaElements elements = interpreter.getPandaElements();
        ParserPipelineRegistry pipelineRegistry = elements.getPipelineRegistry();

        CasualParserGeneration generation = new PandaCasualParserGeneration();
        CommentAssistant commentAssistant = new CommentAssistant();

        ParserInfo parserInfo = new PandaParserInfo();
        parserInfo.setComponent(PandaComponents.ENVIRONMENT, environment);
        parserInfo.setComponent(PandaComponents.PIPELINE_REGISTRY, pipelineRegistry);
        parserInfo.setComponent(PandaComponents.GENERATION, generation);

        for (Source source : sourceSet.getSources()) {
            PandaScript pandaScript = new PandaScript(source.getTitle());
            pandaScript.getImportRegistry().include(defaultModule);

            PandaLexer lexer = new PandaLexer(elements.getSyntax(), source.getContent());
            TokenizedSource tokenizedSource = lexer.convert();

            TokenizedSource uncommentedSource = commentAssistant.uncomment(tokenizedSource);
            PandaSourceStream sourceStream = new PandaSourceStream(uncommentedSource);

            parserInfo = parserInfo.fork();
            parserInfo.setComponent(PandaComponents.SOURCE_STREAM, sourceStream);
            parserInfo.setComponent(PandaComponents.SCRIPT, pandaScript);
            OverallParser overallParser = new OverallParser(parserInfo);

            while (overallParser.hasNext()) {
                overallParser.next();
            }

            application.addScript(pandaScript);
        }

        generation.execute(parserInfo);

        // debug
        for (Script script : application.getScripts()) {
            System.out.println(script.toString());
        }

        return application;
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
