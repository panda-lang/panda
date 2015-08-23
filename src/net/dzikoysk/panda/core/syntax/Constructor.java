package net.dzikoysk.panda.core.syntax;

public interface Constructor<T> {

	public <T> T run(Parameter... parameters);

}
