package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.parser.CustomParser;
import net.dzikoysk.panda.core.parser.ParameterParser;
import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;

public class RunnableBlock extends Block {

	static {
		new BlockScheme(RunnableBlock.class, "runnable").parser(new CustomParser<Block>() {
			@Override
			public Block parse(BlockInfo blockInfo, Block current, Block latest) {
				current = new RunnableBlock();
				current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
				return current;
			}
		});
	}

	public RunnableBlock(){
		super.setName("RunnableBlock");
	}

}
