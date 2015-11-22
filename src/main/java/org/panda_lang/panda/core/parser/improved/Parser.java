package org.panda_lang.panda.core.parser.improved;

import org.panda_lang.panda.core.parser.improved.util.PatternExtractor;
import org.panda_lang.panda.core.parser.improved.util.SourcesDivider;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Executable;

public interface Parser {

    public Executable parse(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous);
    public Block getParent();

}
