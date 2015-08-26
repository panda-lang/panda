package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.parser.CustomParser;
import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

	static {
		new BlockScheme(ElseThenBlock.class, false, "else").parser(new CustomParser<Block>() {
			@Override
			public Block parse(BlockInfo blockInfo, Block current, Block latest) {
				current = new ElseThenBlock();
				if(latest instanceof IfThenBlock){
					((IfThenBlock) latest).setElseThenBlock(current);
				}
				return current;
			}
		});
	}
	
	public ElseThenBlock(){
		super.setName("ElseThenBlock");
	}

}
