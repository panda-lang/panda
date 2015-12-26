package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserCenter;
import org.panda_lang.panda.core.parser.essential.assistant.VariableAssistant;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Variable;
import org.panda_lang.panda.lang.PNull;

public class VariableParser implements Parser {

    static {
        ParserLayout parserLayout = new ParserLayout(new VariableParser(), "*=*;", EssentialPriority.VARIABLE.getPriority());
        parserLayout.pattern(";", EssentialPriority.VARIABLE.getPriority() + 1);
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public Variable parse(Atom atom) {
        String source = atom.getSourcesDivider().getLine();
        String[] ss = VariableAssistant.splitAndClear(source);
        if (ss == null || ss.length < 1) {
            System.out.println("[VariableParser] Cannot parseLocal: " + source);
            return null;
        }

        Parameter parameter = new Parameter("null", new PNull());
        if (ss.length > 1) {
            atom.setSourceCode(ss[1]);
            ParameterParser parser = new ParameterParser();
            parameter = parser.parse(atom);
        }

        String[] lss = ss[0].split(" ");
        String variable = lss.length > 1 ? lss[1] : lss[0];

        if (parameter.getDataType() == null && lss.length > 1) {
            parameter.setDataType(lss[0]);
        }

        return new Variable(atom.getParent(), variable, parameter);
    }

}
