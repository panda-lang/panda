package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.depracted.util.BlockInfo;
import org.panda_lang.panda.core.syntax.Block;

public interface CustomParser<T> {

    public <T> T parse(BlockInfo blockInfo, Block current, Block latest);

}
