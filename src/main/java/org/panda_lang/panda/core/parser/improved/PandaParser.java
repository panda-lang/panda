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
    private boolean exception;

    public PandaParser(String source) {
        this.pandaScript = new PandaScript();
        this.divider = new SourcesDivider(source);
        this.extractor = new PatternExtractor();
        this.pandaBlock = new PandaBlock();
    }

    public PandaScript parse() {
        while(divider.hasNext() && isHappy()) {
            // {prepare}
            String line = divider.next();
            String pattern = extractor.extract(line, PatternExtractor.DEFAULT);
            ParserScheme scheme = ElementsBucket.getParserScheme(pattern);

            // {parser.not.found}
            if(scheme == null) {
                throwException(new Error("ParserNotFoundException", line, divider.getLineNumber(), divider.getCaretPosition()));
                return null;
            }

            Parser parser = scheme.getParser();
            Executable executable = parser.parse(this, divider, extractor, pandaBlock, pandaBlock);
            pandaBlock.addExecutable(executable);
        }
        return pandaScript;
    }

    public Executable throwException(Error error) {
        error.print();
        exception = true;
        return null;
    }

    public boolean isHappy() {
        return !exception;
    }

    public PandaBlock getPandaBlock() {
        return pandaBlock;
    }

}
