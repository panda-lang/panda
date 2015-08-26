package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.parser.CustomParser;
import net.dzikoysk.panda.core.parser.ParameterParser;
import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.lang.PBoolean;
import net.dzikoysk.panda.lang.PObject;

public class WhileBlock extends Block {

	static {
		new BlockScheme(WhileBlock.class, "while").parser(new CustomParser<Block>() {
			@Override
			public Block parse(BlockInfo blockInfo, Block current, Block latest) {
				current = new WhileBlock();
				current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
				return current;
			}
		});
	}

	public WhileBlock(){
		super.setName("WhileBlock");
	}

	@Override
	public PObject run(Parameter... vars) {
		while(((PBoolean) parameters[0].getValue()).isTrue()){
			PObject o = super.run(vars);
			if(o != null) return o;
		} return null;
	}

}
