package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.PandaParser;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.util.PatternExtractor;
import org.panda_lang.panda.core.parser.improved.util.SourcesDivider;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Block;

public class VialParser implements Parser {

    static {
        ParserScheme scheme = new ParserScheme(new VialParser(), "{");
        ElementsBucket.registerParser(scheme);
    }

    private Block parent;

    @Override
    public Block parse(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous) {
        this.parent = parent;

        return null;
    }

    @Override
    public Block getParent() {
        return parent;
    }

}
