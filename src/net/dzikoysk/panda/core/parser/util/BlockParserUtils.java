package net.dzikoysk.panda.core.parser.util;

import java.util.ArrayList;
import java.util.List;

public class BlockParserUtils {

	public static BlockInfo getSectionIndication(String s){
		String name = null;
		List<String> specifiers = new ArrayList<>();
		List<String> parameters = new ArrayList<>();

		// {build.tools}
		StringBuilder node = new StringBuilder();
		boolean string = false;
		boolean spec = false;
		boolean parm = false;

		// {parse}
		char[] chars = s.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			char c = chars[i];

			// {switch.special}
			switch (c) {
				case '"':
					// {string.skip}
					string = !string;
					break;
				case '(':
					// {parameters.start}
					if(!string) spec = true;
				case ')':
				case ',':
				case '{':
					if(!string) c = ' ';
			}

			// {part.end}
			if (Character.isWhitespace(c)) {

				// {empty.continue}
				if (node.length() == 0) continue;

				// {part.append}
				String part = node.toString();
				if (name == null) name = part;
				else if(parm) parameters.add(part);
				else {
					if(spec) parm = true;
					specifiers.add(part);
				}

				// {clear}
				node.setLength(0);
				continue;
			}

			// {default.append}
			node.append(c);
		}

		// build {blockinfo}
		return new BlockInfo(name, specifiers, parameters);
	}

}
