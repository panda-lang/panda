/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.implementation.interpreter.parser.util;

import org.panda_lang.framework.interpreter.SourceFile;
import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.Parser;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.ParserPipeline;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Script;
import org.panda_lang.framework.structure.Statement;
import org.panda_lang.framework.structure.Wrapper;
import org.panda_lang.framework.util.FileUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.parser.OverallParser;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserInfo;
import org.panda_lang.panda.implementation.interpreter.parser.ParserComposition;
import org.panda_lang.panda.implementation.interpreter.parser.linker.PandaWrapperLinker;
import org.panda_lang.panda.implementation.structure.PandaScript;

import java.io.File;

public class SourceFileParser implements Parser {

    private final PandaInterpreter interpreter;

    public SourceFileParser(PandaInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    public Script parse(SourceFile sourceFile) {
        File file = sourceFile.getFile();
        String scriptName = FileUtils.getFileName(file);

        PandaScript pandaScript = new PandaScript(scriptName);

        Panda panda = interpreter.getPanda();
        PandaComposition pandaComposition = panda.getPandaComposition();
        ParserComposition parserComposition = pandaComposition.getParserComposition();
        ParserPipeline pipeline = parserComposition.getPipeline();

        PandaLexer lexer = new PandaLexer(pandaComposition.getSyntaxComposition(), sourceFile.getContent());
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        ParserInfo parserInfo = new PandaParserInfo();
        parserInfo.setComponent(Components.INTERPRETER, interpreter);
        parserInfo.setComponent(Components.PARSER_PIPELINE, pipeline);
        parserInfo.setComponent(Components.READER, tokenReader);
        parserInfo.setComponent(Components.LINKER, new PandaWrapperLinker());

        OverallParser headOverallParser = new OverallParser(parserInfo, tokenReader);

        for (Statement statement : headOverallParser) {
            if (!(statement instanceof Wrapper)) {
                tokenReader.synchronize();
                throw new PandaParserException("Illegal statement in the main scope at " + tokenReader.previous().getLine());
            }

            Wrapper wrapper = (Wrapper) statement;
            pandaScript.addWrapper(wrapper);
        }

        return pandaScript;
    }

    public PandaInterpreter getInterpreter() {
        return interpreter;
    }

}
