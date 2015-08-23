package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.core.scheme.ConstructorScheme;
import net.dzikoysk.panda.core.scheme.MethodScheme;
import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.Constructor;
import net.dzikoysk.panda.core.syntax.IExecutable;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.block.RunnableBlock;

public class PRunnable extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PRunnable.class, "Runnable");
		// Constructor
		os.registerConstructor(new ConstructorScheme(new Constructor<PRunnable>() {
			@Override
			public PRunnable run(Parameter... parameters) {
				return new PRunnable();
			}
		}));
		// Method: run
		os.registerMethod(new MethodScheme("run", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				return instance.getValue(PRunnable.class).run(parameters);
			}
		}));
	}

	private RunnableBlock block;

	public PObject run(Parameter... vars){
		return block != null ? block.run(vars) : null;
	}

	public void setBlock(RunnableBlock block){
		this.block = block;
	}

	@Override
	public String getType(){
		return "Runnable";
	}

}
