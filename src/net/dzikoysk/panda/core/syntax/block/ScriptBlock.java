package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;

public class ScriptBlock extends Block {
	
	static {
		new BlockScheme(ScriptBlock.class, "class");
	}

	public ScriptBlock(){
		super.setName("ScriptBlock");
	}

}
