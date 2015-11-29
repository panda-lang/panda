package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.scheme.ParserScheme;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Variable;

public class VariableParser implements Parser {

    static {
        ParserScheme parserScheme = new ParserScheme(new VariableParser(), "*=*;", EssentialPriority.VARIABLE.getPriority());
        ElementsBucket.registerParser(parserScheme);
    }

    @Override
    public Variable parse(Atom atom) {
        String source = atom.getSourcesDivider().getLine();
        String[] ss = splitAndClear(source);
        if (ss == null || ss.length != 2) {
            System.out.println("[VariableParser] Cannot parseLocal: " + source);
            return null;
        }

        atom.setSourceCode(ss[1]);
        ParameterParser parser = new ParameterParser();
        Parameter parameter = parser.parse(atom);

        if (parameter.getDataType() == null) {
            String[] lss = ss[0].split(" ");
            parameter.setDataType(lss[0]);
        }

        return new Variable(atom.getParent(), ss[0], parameter);
    }

    public String[] splitAndClear(String source) {
        String[] ss = source.split("=", 2);
        if (ss.length != 2) {
            return null;
        }
        ss[0] = clear(ss[0]);
        ss[1] = clear(ss[1]);
        return ss;
    }

    public String clear(String s) {
        StringBuilder node = new StringBuilder();
        boolean string = false;
        for (char c : s.toCharArray()) {
            if (!string && Character.isWhitespace(c)) {
                if (!node.toString().equals("new")) {
                    continue;
                } else {
                    node.append(c);
                    continue;
                }
            } else if (!string && c == ';') {
                continue;
            } else {
                if (c == '"') {
                    string = !string;
                }
                node.append(c);
            }
        }
        return node.toString();
    }

}
