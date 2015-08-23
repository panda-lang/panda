package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.core.scheme.ConstructorScheme;
import net.dzikoysk.panda.core.scheme.MethodScheme;
import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.Constructor;
import net.dzikoysk.panda.core.syntax.IExecutable;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.block.ThreadBlock;

public class PThread extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PThread.class, "Thread");
		// Constructor
		os.registerConstructor(new ConstructorScheme(new Constructor<PThread>() {
			@Override
			public PThread run(Parameter... parameters) {
				return parameters != null && parameters.length != 0 ? new PThread(parameters[0].getValue().toString()) : new PThread();
			}
		}));
		// Method: start
		os.registerMethod(new MethodScheme("start", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				PThread thread = instance.getValue(PThread.class);
				thread.start(parameters);
				return null;
			}
		}));
		// Method: getName
		os.registerMethod(new MethodScheme("getName", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				PThread pThread = instance.getValue(PThread.class);
				return new PString(pThread.getName());
			}
		}));
		// Static method: currentThread
		os.registerMethod(new MethodScheme("currentThread", new IExecutable() {
			@Override
			public PObject run(Parameter instance, Parameter... parameters) {
				return new PThread(Thread.currentThread());
			}
		}));
	}

	private String name;
	private ThreadBlock block;
	private Thread thread;

	public PThread(){
	}

	public PThread(String name){
		this.name = name;
	}

	public PThread(Thread thread){
		this.thread = thread;
		this.name = thread.getName();
	}

	public void start(Parameter... vars){
		if(this.block != null) block.start(vars);
	}

	public void setBlock(ThreadBlock block){
		this.block = block;
	}

	public String getName(){
		return name != null ? name : "ThreadBlock";
	}

	@Override
	public String getType(){
		return "Thread";
	}

}
