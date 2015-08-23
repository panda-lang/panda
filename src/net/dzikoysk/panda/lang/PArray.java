package net.dzikoysk.panda.lang;

import net.dzikoysk.panda.core.scheme.ObjectScheme;
import net.dzikoysk.panda.core.syntax.Parameter;

public class PArray extends PObject {

	static {
		// Register object
		ObjectScheme os = new ObjectScheme(PArray.class, "Array");
	}

	private final Parameter[] array;

	public PArray(Parameter... values){
		this.array = values;
	}

}
