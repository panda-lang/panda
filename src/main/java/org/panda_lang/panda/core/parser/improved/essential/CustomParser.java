package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.improved.essential.util.BlockInfo;
import org.panda_lang.panda.core.syntax.Block;

public interface CustomParser {

    public Block parse(BlockInfo blockInfo, Block parent, Block current, Block previous);

}
