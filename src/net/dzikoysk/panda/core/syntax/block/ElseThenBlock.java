package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

	static {
		new BlockScheme(ElseThenBlock.class, false, "else");
	}
	
	public ElseThenBlock(){
		super.setName("ElseThenBlock");
	}

}
