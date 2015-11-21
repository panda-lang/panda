package org.panda_lang.panda.core.parser.improved;

import org.panda_lang.panda.core.parser.improved.util.SourcesDivider;
import org.panda_lang.panda.core.syntax.Block;

public class VialParser implements Parser {

    private Block parent;

    @Override
    public Block parse(PandaParser pandaParser, SourcesDivider sourcesDivider, Block parent, Block previous) {
        this.parent = parent;
        return null;
    }

    @Override
    public Block getParent() {
        return parent;
    }

}
