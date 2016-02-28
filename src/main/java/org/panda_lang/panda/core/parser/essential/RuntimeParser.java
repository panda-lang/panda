package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Runtime;

public class RuntimeParser implements Parser {

    @Override
    public Factor parse(Atom atom) {
        String parameter = atom.getSourceCode();

        // Math
        if (FactorAssistant.isMath(parameter)) {
            atom.getSourcesDivider().setLine(parameter);
            return new MathParser().parse(atom);
        }
        // Equality
        else if (FactorAssistant.isEquality(atom, parameter)) {
            atom.getSourcesDivider().setLine(parameter);
            return new EqualityParser().parse(atom);
        }
        else if (FactorAssistant.isMethod(atom, parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') {
                Runtime runtime = parseConstructor(atom, parameter);
                return new Factor(runtime);
            }
            // Method
            Runtime runtime = parseMethod(atom, parameter);
            return new Factor(runtime);
        }

        return null;
    }

    public Runtime parseConstructor(Atom atom, String source) {
        ConstructorParser constructorParser = new ConstructorParser();
        atom.setSourceCode(source);
        return constructorParser.parse(atom);
    }

    public Runtime parseMethod(Atom atom, String source) {
        MethodParser parser = new MethodParser();
        atom.getSourcesDivider().setLine(source);
        return parser.parse(atom);
    }

}
