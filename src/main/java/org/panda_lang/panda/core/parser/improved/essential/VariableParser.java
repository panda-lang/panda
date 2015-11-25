package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.improved.PandaParser;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.PatternExtractor;
import org.panda_lang.panda.core.parser.improved.SourcesDivider;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Variable;

public class VariableParser implements Parser {

    static {

    }

    private Block parent;

    @Override
    public Variable parse(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous) {
        this.parent = parent;

        String source = sourcesDivider.getLine();
        String[] ss = splitAndClear(source);
        if (ss == null || ss.length != 2) {
            System.out.println("[VariableParser] Cannot parseLocal: " + source);
            return null;
        }

        ParameterParser parser = new ParameterParser();
        Parameter parameter = parser.parse(pandaParser, new SourcesDivider(ss[1]), extractor, parent, previous);

        if (parameter.getDataType() == null) {
            String[] lss = ss[0].split(" ");
            parameter.setDataType(lss[0]);
        }

        return new Variable(parent, ss[0], parameter);
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

    @Override
    public Block getParent() {
        return parent;
    }

}
