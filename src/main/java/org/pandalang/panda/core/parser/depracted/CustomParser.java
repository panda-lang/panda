package org.pandalang.panda.core.parser.depracted;

import org.panda_lang.panda.core.parser.depracted.util.BlockInfo;
import org.panda_lang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Block;

public interface CustomParser<T> {

    public <T> T parse(BlockInfo blockInfo, Block current, Block latest);

}
