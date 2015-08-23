package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.lang.PBoolean;
import net.dzikoysk.panda.lang.PObject;

public class IfThenBlock extends Block {

	static {
		new BlockScheme(IfThenBlock.class, "if", "else if");
	}

	private Block elseThenBlock;

	public IfThenBlock(){
		super.setName("IfThenBlock");
	}

	@Override
	public PObject run(Parameter... vars) {
		PObject object = parameters[0].getValue();
		if(object instanceof PBoolean){
			PBoolean b = (PBoolean) object;
			if(b.isTrue())
			{
				return super.run(vars);
			}
			else if(elseThenBlock != null)
			{
				return elseThenBlock.run(vars);
			}
		} 
		return null;
	}

	public void setElseThenBlock(Block block){
		this.elseThenBlock = block;
	}

}
