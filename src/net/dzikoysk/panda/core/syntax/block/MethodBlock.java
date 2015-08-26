package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.parser.CustomParser;
import net.dzikoysk.panda.core.parser.ParameterParser;
import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.lang.PObject;

public class MethodBlock extends Block {

	static {
		new BlockScheme(MethodBlock.class, "method").parser(new CustomParser<MethodBlock>(){
			@Override
			public MethodBlock parse(BlockInfo blockInfo, Block current, Block latest) {
				MethodBlock block = new MethodBlock(blockInfo.getSpecifiers().get(0));
				block.setParameters(new ParameterParser().parse(block, blockInfo.getParameters()));
				return block;
			}
		});
	}

	public MethodBlock(String name) {
		super.setName(name);
	}

	@Override
	public PObject run(Parameter... vars) {
		if(parameters != null && (vars == null || vars.length != parameters.length)){
			System.out.println("[MethodBlock] " + getName() +": Bad parameters!");
			return null;
		}
		return super.run(vars);
	}

}
