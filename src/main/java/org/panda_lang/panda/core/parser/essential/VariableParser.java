package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Field;

public class VariableParser implements Parser {

    @Override
    public RuntimeValue parse(ParserInfo parserInfo) {
        String parameter = parserInfo.getSourceCode();

        // Variable
        RuntimeValue variable = new RuntimeValue(parameter);
        // <temp>
        for (Field field : parserInfo.getCurrent().getFields()) {
            if (field.getName().equals(parameter)) {
                variable.setDataType(field.getDataType());
                return variable;
            }
        }
        if (parserInfo.getCurrent().getParent() != null) {
            for (Field field : parserInfo.getCurrent().getParent().getFields()) {
                if (field.getName().equals(parameter)) {
                    variable.setDataType(field.getDataType());
                    return variable;
                }
            }
        }
        return variable;
    }

}
