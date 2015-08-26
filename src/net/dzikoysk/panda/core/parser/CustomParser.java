package net.dzikoysk.panda.core.parser;

import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.syntax.Block;

public interface CustomParser<T> {

	public <T> T parse(BlockInfo blockInfo, Block current, Block latest);

}
