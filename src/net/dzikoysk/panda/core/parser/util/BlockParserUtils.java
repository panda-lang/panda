package net.dzikoysk.panda.core.parser.util;

public class BlockParserUtils {

	public static BlockInfo getSectionIndication(String s){
		StringBuilder args = new StringBuilder();
		StringBuilder node = new StringBuilder();
		String name = null;
		boolean string = false;

		char[] chars = s.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];

			switch (c) {
				case '"':
					string = !string;
					break;
				case '(':
				case ')':
				case ',':
				case '{':
					if(!string) c = ' ';
			}
			if (Character.isWhitespace(c)) {
				if (node.length() == 0){
					continue;
				}
				String part = node.toString().toLowerCase();
				if (!part.equals("method")) {
					if (name == null) {
						name = node.toString();
						node.setLength(0);
						continue;
					}
					if (args.length() != 0){
						args.append(",");
					}
					args.append(part);
				}
				node.setLength(0);
				continue;
			}
			node.append(c);
		}
		String[] params = args.toString().split(",");
		if(params[0].isEmpty()) params = new String[0];
		return new BlockInfo(name, params);
	}

}
