package net.dzikoysk.panda.core.parser;

import net.dzikoysk.panda.core.parser.util.MathParserUtils;
import net.dzikoysk.panda.core.syntax.Block;
import net.dzikoysk.panda.core.syntax.Math;
import net.dzikoysk.panda.core.syntax.Parameter;
import net.dzikoysk.panda.core.syntax.Runtime;
import net.dzikoysk.panda.util.MathBuilder;

import java.util.Stack;
import java.util.StringTokenizer;

public class MathParser {

	private final String source;

	public MathParser(String source){
		this.source = source;
	}

	public Parameter parse(Block block){
		MathBuilder mathBuilder = new MathBuilder();
		Stack<Character> operators = new Stack<>();
		StringTokenizer tokenizer = new StringTokenizer(source, "+-*/^()", true);
		while (tokenizer.hasMoreElements()){
			String token = tokenizer.nextToken();
			if(token.isEmpty()) continue;

			char c = token.charAt(0);
			switch (c){
				case '+':
				case '-':
				case '*':
				case '/':
				case '^':
					if(operators.size() != 0)
						if(MathParserUtils.compare(operators.peek(), c))
							mathBuilder.append(operators.pop());
					operators.push(c);
					break;
				case '(':
					operators.push(c);
					break;
				case ')':
					while(operators.peek() != '(')
						mathBuilder.append(operators.pop());
					operators.pop();
					break;
				default:
					Parameter parameter = new ParameterParser().parse(block, token);
					mathBuilder.append(parameter);
					break;
			}

		} while (operators.size() != 0) mathBuilder.append(operators.pop());

		mathBuilder.rewrite();
		Math math = new Math(mathBuilder);
		return new Parameter("Number", block, new Runtime(math));
	}


}
