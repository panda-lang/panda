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

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.design.architecture.PandaApplication;
import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.PandaInterpreter;
import org.panda_lang.panda.design.interpreter.parser.PandaParserInfo;
import org.panda_lang.panda.design.interpreter.parser.generation.PandaCasualParserGeneration;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.elements.PandaElements;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.architecture.Script;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.language.structure.general.comment.CommentAssistant;

public class SourceParser implements Parser {

    private final PandaInterpreter interpreter;
    private final PandaApplication application;

    public SourceParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
        this.application = interpreter.getApplication();
    }

    public void parse(SourceSet sourceSet) {
        Panda panda = interpreter.getPanda();
        PandaElements elements = panda.getPandaElements();
        ParserPipelineRegistry pipelineRegistry = elements.getPipelineRegistry();

        CasualParserGeneration generation = new PandaCasualParserGeneration();
        CommentAssistant commentAssistant = new CommentAssistant();

        ParserInfo parserInfo = new PandaParserInfo();
        parserInfo.setComponent(Components.PANDA, panda);
        parserInfo.setComponent(Components.INTERPRETER, interpreter);
        parserInfo.setComponent(Components.PIPELINE_REGISTRY, pipelineRegistry);
        parserInfo.setComponent(Components.GENERATION, generation);

        for (Source source : sourceSet.getSources()) {
            PandaScript pandaScript = new PandaScript(source.getTitle());

            PandaLexer lexer = new PandaLexer(elements.getSyntax(), source.getContent());
            TokenizedSource tokenizedSource = lexer.convert();

            TokenizedSource uncommentedSource = commentAssistant.uncomment(tokenizedSource);
            PandaSourceStream sourceStream = new PandaSourceStream(uncommentedSource);

            parserInfo = parserInfo.fork();
            parserInfo.setComponent(Components.SOURCE_STREAM, sourceStream);
            parserInfo.setComponent(Components.SCRIPT, pandaScript);
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
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
