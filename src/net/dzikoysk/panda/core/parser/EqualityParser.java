package net.dzikoysk.panda.core.parser;

import net.dzikoysk.panda.core.syntax.*;
import net.dzikoysk.panda.core.syntax.Runtime;
import net.dzikoysk.panda.util.EqualityBuilder;

public class EqualityParser {

	private final String source;

	public EqualityParser(String source){
		this.source = source;
	}

	public Parameter parse(Block block){
		EqualityBuilder equalityBuilder = new EqualityBuilder();

		Equality equality = new Equality(null);
		return new Parameter("Boolean", block, new Runtime(equality));
	}

}
