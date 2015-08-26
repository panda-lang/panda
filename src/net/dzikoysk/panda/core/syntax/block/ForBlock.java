package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.parser.CustomParser;
import net.dzikoysk.panda.core.parser.ParameterParser;
import net.dzikoysk.panda.core.parser.util.BlockInfo;
import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.lang.PNumber;
import net.dzikoysk.panda.lang.PObject;

public class ForBlock extends Block {

	static {
		new BlockScheme(ForBlock.class, "for").parser(new CustomParser<Block>() {
			@Override
			public Block parse(BlockInfo blockInfo, Block current, Block latest) {
				current = new ForBlock();
				current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
				return current;
			}
		});
	}

	public ForBlock(){
		super.setName("ForBlock");
	}

	@Override
	public PObject run(Parameter... vars) {
		PObject object = parameters[0].getValue();
		if(object instanceof PNumber){
			Number times = ((PNumber) object).getNumber();
			for(int i = 0; i < times.intValue(); i++){
				PObject o = super.run(vars);
				if(o != null) return o;
			}
		}
		return null;
	}

}
