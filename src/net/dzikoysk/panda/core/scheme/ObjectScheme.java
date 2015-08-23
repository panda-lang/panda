package net.dzikoysk.panda.core.scheme;

import net.dzikoysk.panda.core.ElementsBucket;
import net.dzikoysk.panda.lang.PObject;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectScheme {

	private final String name;
	private final Class<? extends PObject> clazz;
	private final Collection<MethodScheme> methods;
	private ConstructorScheme constructor;

	public ObjectScheme(Class<? extends PObject> clazz, String name){
		this.clazz = clazz;
		this.name = name;
		this.methods = new ArrayList<>();
		ElementsBucket.registerObject(this);
	}

	public void registerConstructor(ConstructorScheme constructor){
		this.constructor = constructor;
	}

	public void registerMethod(MethodScheme method){
		this.methods.add(method);
	}

	public Collection<MethodScheme> getMethods() {
		return methods;
	}

	public ConstructorScheme getConstructorScheme() {
		return constructor;
	}

	public Class<? extends PObject> getClazz(){
		return clazz;
	}

	public String getName(){
		return name;
	}

}
