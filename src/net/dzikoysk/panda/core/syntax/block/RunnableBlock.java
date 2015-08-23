package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;

public class RunnableBlock extends Block {

	static {
		new BlockScheme(RunnableBlock.class, "runnable");
	}

	public RunnableBlock(){
		super.setName("RunnableBlock");
	}

}
