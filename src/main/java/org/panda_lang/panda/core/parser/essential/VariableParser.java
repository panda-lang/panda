package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.statement.Field;

public class VariableParser implements Parser {

    @Override
    public Factor parse(Atom atom) {
        String parameter = atom.getSourceCode();

        // Variable
        Factor variable = new Factor(parameter);
        // <temp>
        for (Field field : atom.getCurrent().getFields()) {
            if (field.getName().equals(parameter)) {
                variable.setDataType(field.getDataType());
                return variable;
            }
        }
        if (atom.getCurrent().getParent() != null) {
            for (Field field : atom.getCurrent().getParent().getFields()) {
                if (field.getName().equals(parameter)) {
                    variable.setDataType(field.getDataType());
                    return variable;
                }
            }
        }
        return variable;
    }

}
