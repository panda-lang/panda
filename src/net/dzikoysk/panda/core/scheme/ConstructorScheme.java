package net.dzikoysk.panda.core.scheme;

import net.dzikoysk.panda.core.syntax.Constructor;
import net.dzikoysk.panda.lang.PObject;

public class ConstructorScheme {

	private final Constructor<? extends PObject> constructor;

	public ConstructorScheme(Constructor<? extends PObject> constructor){
		this.constructor = constructor;
	}

	public Constructor<? extends PObject> getConstructor() {
		return constructor;
	}

}
