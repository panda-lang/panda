package org.panda_lang.panda.implementation.interpreter.parser.util;

import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserContext;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.ParserPipeline;
import org.panda_lang.core.interpreter.parser.util.Components;
import org.panda_lang.core.structure.Script;
import org.panda_lang.core.structure.Statement;
import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.PandaComposition;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.interpreter.parser.*;
import org.panda_lang.panda.implementation.structure.PandaScript;
import org.panda_lang.panda.implementation.structure.PandaWrapperLinker;

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

        PandaLexer lexer = new PandaLexer(interpreter.getPanda(), sourceFile.getContent());
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        ParserContext parserContext = new PandaParserContext(lexer.getSource(), tokenizedSource);
        parserContext.setTokenReader(tokenReader);

        ParserInfo parserInfo = new PandaParserInfo();
        parserInfo.setComponent(Components.INTERPRETER, interpreter);
        parserInfo.setComponent(Components.PARSER_PIPELINE, pipeline);
        parserInfo.setComponent(Components.PARSER_CONTEXT, parserContext);
        parserInfo.setComponent(Components.WRAPPER_LINKER, new PandaWrapperLinker());

        OverallParser headOverallParser = new OverallParser(parserInfo);

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
