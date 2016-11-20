package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.*;
import org.panda_lang.core.interpreter.parser.util.Components;
import org.panda_lang.core.structure.Script;
import org.panda_lang.core.structure.Statement;
import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.util.FileUtils;
import org.panda_lang.panda.implementation.interpreter.PandaInterpreter;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;
import org.panda_lang.panda.implementation.structure.PandaScript;

import java.io.File;

public class PandaParser implements Parser {

    private final PandaInterpreter interpreter;
    private final ParserPipeline pipeline;

    public PandaParser(PandaInterpreter interpreter, ParserPipeline pipeline) {
        this.interpreter = interpreter;
        this.pipeline = pipeline;
    }

    public Script parse(SourceFile sourceFile) {
        File file = sourceFile.getFile();

        String scriptName = FileUtils.getFileName(file);
        PandaScript pandaScript = new PandaScript(scriptName);

        PandaLexer lexer = new PandaLexer(interpreter.getPanda(), sourceFile.getContent());
        TokenizedSource tokenizedSource = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);

        ParserContext parserContext = new PandaParserContext(lexer.getSource(), tokenizedSource);
        parserContext.setTokenReader(tokenReader);

        ParserInfo parserInfo = new PandaParserInfo();
        parserInfo.setComponent(Components.INTERPRETER, interpreter);
        parserInfo.setComponent(Components.PARSER_PIPELINE, pipeline);
        parserInfo.setComponent(Components.PARSER_CONTEXT, parserContext);

        while (tokenReader.hasNext()) {
            tokenReader.synchronize();

            ContainerParser parser = pipeline.handle(tokenReader);

            if (parser == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Unrecognized " + tokenReader.previous().getLine());
            }

            Statement statement = parser.parse(parserInfo);

            if (statement == null) {
                tokenReader.synchronize();
                throw new PandaParserException("Failed to parse statement at " + tokenReader.previous().getLine());
            }

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
