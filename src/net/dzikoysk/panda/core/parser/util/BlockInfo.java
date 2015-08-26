package net.dzikoysk.panda.core.parser.util;

import java.util.List;

public class BlockInfo {

	private final String type;
	private final List<String> specifiers;
	private final List<String> parametrs;

	public BlockInfo(String type, List<String> specifiers, List<String> parametrs) {
		this.type = type;
		this.specifiers = specifiers;
		this.parametrs = parametrs;
	}

	public String getType() {
		return type;
	}

	public List<String> getSpecifiers() {
		return specifiers;
	}

	public String[] getParameters(){
		String[] params = new String[parametrs.size()];
		return parametrs.toArray(params);
	}

	public List<String> getParametrsList() {
		return parametrs;
	}

}
