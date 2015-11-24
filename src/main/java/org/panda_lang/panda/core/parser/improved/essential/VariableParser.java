package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Variable;

public class VariableParser {

    private final Block block;
    private final String source;

    public VariableParser(Block block, String source) {
        this.block = block;
        this.source = source;
    }

    public Variable parse() {
        String[] ss = splitAndClear(source);
        if(ss == null || ss.length != 2){
            System.out.println("[VariableParser] Cannot parse: " + source);
            return null;
        }

        ParameterParser parser = new ParameterParser(ss[1]);
        Parameter parameter = parser.parse(block, ss[1]);

        if(parameter.getDataType() == null) {
            String[] lss = ss[0].split(" ");
            parameter.setDataType(lss[0]);
        }

        return new Variable(block, ss[0], parameter);
    }

    public String[] splitAndClear(String source) {
        String[] ss = source.split("=", 2);
        if(ss == null || ss.length != 2) {
            return null;
        }
        ss[0] = clear(ss[0]);
        ss[1] = clear(ss[1]);
        return ss;
    }

    public String clear(String s) {
        StringBuilder node = new StringBuilder();
        boolean string = false;
        for(char c : s.toCharArray()) {
            if(!string && Character.isWhitespace(c)) {
                if(!node.toString().equals("new")) {
                    continue;
                } else {
                    node.append(c);
                    continue;
                }
            } else if(!string && c == ';') {
                continue;
            } else {
                if(c == '"') {
                    string = !string;
                }
                node.append(c);
            }
        }
        return node.toString();
    }

}
