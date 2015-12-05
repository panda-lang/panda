package org.pandalang.panda.core.parser.depracted;

import org.panda_lang.panda.core.parser.depracted.util.VariableParserUtils;
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
        String[] ss = VariableParserUtils.splitAndClear(source);
        if (ss == null || ss.length != 2) {
            System.out.println("[VariableParser] Cannot parse: " + source);
            return null;
        }

        ParameterParser parser = new ParameterParser(ss[1]);
        Parameter parameter = parser.parse(block, ss[1]);

        if (parameter.getDataType() == null) {
            String[] lss = ss[0].split(" ");
            parameter.setDataType(lss[0]);
        }

        return new Variable(block, ss[0], parameter);
    }

}
