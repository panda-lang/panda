package net.dzikoysk.panda.core.syntax;

import net.dzikoysk.panda.lang.PObject;

public class Runtime implements Executable {

	private Parameter instance;
	private IExecutable executable;
	private Parameter[] parameters;
	private Method method;
	private Math math;

	public Runtime(Method method){
		this.method = method;
	}

	public Runtime(Math math){
		this.math = math;
	}

	public Runtime(Parameter instance, IExecutable executable, Parameter[] parameters){
		this.instance = instance;
		this.executable = executable;
		this.parameters = parameters;
	}

	@Override
	public PObject run(Parameter... parameters) {
		if(method != null) return method.run(parameters);
		else if(math != null) return math.run(null, parameters);
		return executable.run(instance, this.parameters);
	}

	@Override
	public String getName() {
		return "Runtime";
	}

}
