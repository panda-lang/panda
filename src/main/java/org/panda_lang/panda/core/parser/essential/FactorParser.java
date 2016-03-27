package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Factor;

public class FactorParser implements Parser {

    @Override
    public Factor parse(Atom atom) {
        return parse(atom, atom.getSourceCode());
    }

    public Factor parse(Atom atom, String parameter) {
        atom.setSourceCode(parameter);

        RuntimeParser runtimeParser = new RuntimeParser();
        Factor runtimeFactor = runtimeParser.parse(atom);

        if (runtimeFactor != null) {
            return runtimeFactor;
        }

        EssenceParser essenceParser = new EssenceParser();
        Essence essence = essenceParser.parse(atom);

        if (essence != null) {
            return new Factor(essence);
        }

        VariableParser variableParser = new VariableParser();
        return variableParser.parse(atom);
    }

    public Factor[] parse(Atom atom, String[] parametersSources) {
        Factor[] factors = new Factor[parametersSources.length];
        for (int i = 0; i < factors.length; i++) {
            String src = parametersSources[i];
            if (src == null || src.isEmpty()) {
                continue;
            }
            factors[i] = parse(atom, src);
        }
        return factors;
    }

    public Factor[] splitAndParse(Atom atom) {
        String[] parametersSources = FactorAssistant.split(atom.getSourceCode());
        return parse(atom, parametersSources);
    }

}
