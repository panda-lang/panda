package org.panda_lang.panda.core.parser.depracted.util;

import java.util.Stack;

public class Recognizer {

    public SyntaxIndication recognize(String line){
        StringBuilder node = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        boolean string = false;
        boolean comment = false;
        boolean var = false;
        for(char c : line.toCharArray()){
            switch (c) {
                case '(':
                case ')':
                case '{':
                case '}':
                case '=':
                case '/':
                    if(var || string) continue;
                    if(stack.size() > 0 && stack.peek() == c) continue;
                    if(c == '=') var = true;
                    node.append(c);
                    stack.push(c);
                    break;
                case '"':
                    string = !string;
                    break;
                case ';':
                    node.append(c);
                    String s = node.toString();
                    return SyntaxIndication.recognize(s);
                default:
                    break;
            }
        }
        return SyntaxIndication.recognize(node.toString());
    }

    public String getLineIndication(String line){
        StringBuilder node = new StringBuilder();
        for(char c : line.toCharArray()){
            if(Character.isWhitespace(c) || c == '(' || c == '{'){
                if(node.length() == 0) continue;
                else break;
            } else node.append(c);
        } return node.toString();
    }

}
