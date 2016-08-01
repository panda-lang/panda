package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.Interpreter;
import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserInfo;
import org.panda_lang.core.interpreter.parser.representation.ParserRepresentationPipeline;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.lang.registry.ParserRegistry;

public class PandaParser implements Parser {

    private final Interpreter interpreter;
    private final ParserRepresentationPipeline pipeline;
    private PandaScript pandaScript;

    public PandaParser(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.pipeline = ParserRegistry.getPipeline();
    }

    @Override
    public PandaScript parse(ParserInfo parserInfo) {
        this.pandaScript = new PandaScript();

        /* TODO: ↓
        for (Fragment fragment : divider) {
            ParserRepresentationHandler parserRepresentationHandler = pipeline.handle(fragment);

            if (parserRepresentationHandler == null) {
                ParserError error = new PandaParserError()
                        .title("Unrecognized fragment")
                        .particulars("Fragment: '" + fragment.getFragment() + "'")
                        .line(divider.getLine());

                ParserStatus parserStatus = parserInfo.getParserStatus();
                return parserStatus.throwParserError(error);
            }
        }
        */

        return pandaScript;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

    public ParserRepresentationPipeline getPipeline() {
        return pipeline;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

}
