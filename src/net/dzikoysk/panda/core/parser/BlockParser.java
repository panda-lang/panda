package net.dzikoysk.panda.core.parser;

import net.dzikoysk.panda.PandaScript;
import net.dzikoysk.panda.core.parser.util.*;
import net.dzikoysk.panda.core.parser.util.Error;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Method;
import net.dzikoysk.panda.core.syntax.Variable;
import net.dzikoysk.panda.core.syntax.block.*;

import java.util.Stack;

public class BlockParser {

	private final PandaScript script;
	private final String[] source;
	private final Recognizer recognizer;
	private Block parent, current, latest;
	private int i;

	public BlockParser(PandaScript script, Recognizer recognizer, String section){
		this.script = script;
		this.source = CodePatcher.getAsLines(section);
		this.recognizer = recognizer;
	}

	public Block parse(PandaParser panda){
		Stack<Character> characters = new Stack<>();
		StringBuilder node = new StringBuilder();
		for (i = 0; i < source.length; i++) {
			String line = source[i];
			if(line.isEmpty()) continue;
			if(i == 0){
				String info = recognizer.getLineIndication(line).toLowerCase();
				BlockInfo mi = BlockParserUtils.getSectionIndication(line);
				boolean add = true;
				switch (info){
					case "method":
						current = new MethodBlock(mi.getName());
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
					case "if":
						current = new IfThenBlock();
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
					case "else":
						current = new ElseThenBlock();
						if(latest instanceof IfThenBlock){
							((IfThenBlock) latest).setElseThenBlock(current);
							add = false;
						}
						break;
					case "for":
						current = new ForBlock();
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
					case "while":
						current = new WhileBlock();
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
					case "runnable":
						current = new RunnableBlock();
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
					case "thread":
						current = new ThreadBlock();
						current.setParameters(new ParameterParser().parse(current, mi.getParametrs()));
						break;
				}
				/*
				for(BlockScheme bs : ElementsBucket.getBlocks()){
					if(String s : bs.getIndications()){
						if(s.equals(info)){
							//TO-DO
						}
					}
				}
				*/

				if(current == null){
					System.out.println("[BlockParser] Type not detected: " + line);
					return null;
				}
				if(add) parent.addExecutable(current);
				current.setParent(parent);
				continue;
			}

			SyntaxIndication indi = recognizer.recognize(line);
			if (indi == null) {
				String info = recognizer.getLineIndication(line).toLowerCase();
				if(info.equals("else")) {
					indi = SyntaxIndication.SECTION;
				} else {
					System.out.println("Error at line " + i);
					Error error = new Error("[SyntaxIndication] Not detected: " + line);
					error.print();
					break;
				}
			}
			switch (indi) {
				case CLOSE:
					characters.pop();
					if(characters.size() == 0) {
						String sectionSource = node.toString();
						node.setLength(0);
						BlockParser parser = new BlockParser(script, recognizer, sectionSource);
						parser.setParent(current);
						parser.setLatest(latest);
						Block block = parser.parse(panda);
						latest = block;
						break;
					}
					node.append(line);
					node.append(System.lineSeparator());
					break;
				case SECTION:
					characters.push('{');
				case METHOD:
					if(characters.size() == 0){
						MethodParser parser = new MethodParser(script, current, line);
						Method method = parser.parse();
						current.addExecutable(method);
						break;
					}
				case VARIABLE:
					if(characters.size() == 0){
						VariableParser parser = new VariableParser(current, line);
						Variable variable = parser.parse();
						current.addExecutable(variable);
						break;
					}
				default:
					node.append(line);
					node.append(System.lineSeparator());
					break;
			}
		}
		panda.setCurrentLine(i);
		return current;
	}

	public void setLatest(Block block){
		this.latest = block;
	}

	public void setParent(Block block){
		this.parent = block;
	}

	public int getCurrentLine(){
		return this.i;
	}

}