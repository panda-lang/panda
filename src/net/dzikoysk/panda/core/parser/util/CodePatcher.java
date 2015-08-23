package net.dzikoysk.panda.core.parser.util;

import net.dzikoysk.panda.util.StackUtils;

import java.util.Stack;

public class CodePatcher {

	public static String[] patch(String source){
		StringBuilder node = new StringBuilder();
		boolean string = false,
		        skip = false;
		int cp = -1;

		char[] chars = source.toCharArray();
		for(int i = 0; i < chars.length; i++){
			char c = chars[i];

			if(c == '\n'){
				skip = false;
				cp = -1;
			} else if(skip){
				continue;
			}

			if(c == '"'){
				string = !string;
			} else if(string){
				node.append(c);
				continue;
			} else if(c == '/'){
				if(cp == -1){
					cp = i;
					continue;
				} else if (cp == i - 1){
					skip = true;
					continue;
				} else {
					node.append(c);
					skip = false;
					cp = -1;
				}
			} else if(Character.isWhitespace(c) && node.length() == 0){
				continue;
			}

			node.append(c);
			if(!string && (c == '{' || c == ';' || c == '}')){
				node.append(System.lineSeparator());
			}
		}
		String[] spl = node.toString().split(System.lineSeparator());
		Stack<String> lines = new Stack<>();
		for(String line : spl){
			String s = line.trim();
			if(s.isEmpty() || s.startsWith("//")){
				continue;
			}
			lines.push(line);
		}
		return StackUtils.toArray(String.class, lines);
	}

	public static String[] getAsLines(String s){
		return s.split(System.lineSeparator());
	}

}
