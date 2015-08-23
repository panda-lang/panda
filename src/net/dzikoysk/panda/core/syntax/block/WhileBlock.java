package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.lang.PBoolean;
import net.dzikoysk.panda.lang.PObject;

public class WhileBlock extends Block {

	static {
		new BlockScheme(WhileBlock.class, "while");
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
