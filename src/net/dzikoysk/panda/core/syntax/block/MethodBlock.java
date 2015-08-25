package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.lang.PObject;

public class MethodBlock extends Block {

	static {
		new BlockScheme(MethodBlock.class, "method");
	}

	public MethodBlock(String name) {
		super.setName(name);
	}

	@Override
	public PObject run(Parameter... vars) {
		if(vars != null){
			for(int i = 0; i < vars.length; i++){
				parameters[i].setValue(vars[i].getValue());
			}
		}
		if(parameters != null && (vars == null || vars.length != parameters.length)){
			System.out.println("[MethodBlock] " + getName() +": Bad parameters!");
			return null;
		}
		return super.run(vars);
	}

}
