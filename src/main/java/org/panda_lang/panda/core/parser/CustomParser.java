package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.parser.util.BlockInfo;
import org.panda_lang.panda.core.syntax.Block;

public interface CustomParser<T> {

    public <T> T parse(BlockInfo blockInfo, Block current, Block latest);

}
