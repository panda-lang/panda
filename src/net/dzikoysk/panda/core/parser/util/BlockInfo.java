package net.dzikoysk.panda.core.parser.util;

public class BlockInfo {

	private final String name;
	private final String[] parametrs;

	public BlockInfo(String name, String[] parametrs) {
		this.name = name;
		this.parametrs = parametrs;
	}

	public String getName() {
		return name;
	}

	public String[] getParametrs() {
		return parametrs;
	}

}
