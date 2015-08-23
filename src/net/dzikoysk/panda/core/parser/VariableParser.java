package net.dzikoysk.panda.core.parser;

import net.dzikoysk.panda.core.parser.util.VariableParserUtils;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.Variable;

public class VariableParser {

	private final Block block;
	private final String source;

	public VariableParser(Block block, String source){
		this.block = block;
		this.source = source;
	}

	public Variable parse(){
		String[] ss = VariableParserUtils.splitAndClear(source);
		if(ss == null || ss.length != 2){
			System.out.println("[VariableParser] Cannot parse: " + source);
			return null;
		}

		ParameterParser parser = new ParameterParser(ss[1]);
		Parameter parameter = parser.parse(block, ss[1]);
		return new Variable(block, ss[0], parameter);
	}

}
