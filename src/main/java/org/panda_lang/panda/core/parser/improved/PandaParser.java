package org.panda_lang.panda.core.parser.improved;

/*

    Improved version of the Panda Parser.

 */

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.util.Error;
import org.panda_lang.panda.core.parser.improved.util.SourcesDivider;
import org.panda_lang.panda.core.parser.improved.util.PatternExtractor;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

public class PandaParser {

    private final PandaScript pandaScript;
    private final SourcesDivider divider;
    private final PatternExtractor extractor;
    private final PandaBlock pandaBlock;

    public PandaParser(String source) {
        this.pandaScript = new PandaScript();
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.pandaBlock = new PandaBlock();
    }

    public PandaScript parse() {
        while(divider.hasNext()) {
            // {prepare}
            String line = divider.next();
            String pattern = extractor.pattern(line);
            ParserScheme scheme = ElementsBucket.getParserScheme(pattern);

            // {parser.not.found}
            if(scheme == null) {
                Error error = new Error("ParserNotFoundException", line, divider.getLineNumber(), divider.getCaretPosition());
                error.print();
                return null;
            }

            Parser parser = scheme.getParser();
            Executable executable = parser.parse(this, divider, pandaBlock, pandaBlock);
            pandaBlock.addExecutable(executable);
        }
        return pandaScript;
    }

    public PandaBlock getPandaBlock() {
        return pandaBlock;
    }

}
