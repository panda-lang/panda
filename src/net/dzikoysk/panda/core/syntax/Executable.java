package net.dzikoysk.panda.core.syntax;

import net.dzikoysk.panda.lang.PObject;

public interface Executable {

	public PObject run(Parameter... parameters);
	public String getName();

}
