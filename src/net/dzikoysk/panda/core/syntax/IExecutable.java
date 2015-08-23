package net.dzikoysk.panda.core.syntax;

import net.dzikoysk.panda.lang.PObject;

public interface IExecutable {

	public PObject run(Parameter instance, Parameter... parameters);

}
