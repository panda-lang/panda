package net.dzikoysk.panda.core.syntax.block;

import net.dzikoysk.panda.core.scheme.BlockScheme;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Variable;

import java.util.ArrayList;
import java.util.Collection;

public class ScriptBlock extends Block {
	
	static {
		new BlockScheme(ScriptBlock.class, "class");
	}

	private final Collection<Variable> variables;

	public ScriptBlock(){
		super.setName("ScriptBlock");
		this.variables = new ArrayList<>();
	}

	public void initVariables(){
		for(Variable variable : variables){
			variable.run();
		}
	}

	public void addVariable(Variable variable){
		this.variables.add(variable);
	}

}
