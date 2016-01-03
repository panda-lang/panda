package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.PandaException;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.ParameterAssistant;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.core.syntax.block.VialBlock;
import org.panda_lang.panda.lang.PArray;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PString;

public class ParameterParser implements Parser {

    @Override
    public Factor parse(Atom atom) {
        return parse(atom, atom.getSourceCode());
    }

    public Factor parse(Atom atom, String parameter) {
        if (ParameterAssistant.isMath(parameter)) {
            // Math
            MathParser parser = new MathParser(parameter);
            return parser.parse(atom);
        } else if (ParameterAssistant.isMethod(atom, parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') return parseConstructor(atom, parameter);
            // Method
            MethodParser parser = new MethodParser();
            atom.getSourcesDivider().setLine(parameter);
            Runtime methodRuntime = parser.parse(atom);
            return new Factor(null, atom.getParent().getVariables(), methodRuntime);
        }

        char[] chars = parameter.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') return parseString(parameter);
            // Array
        else if (c == '[') return parseArray(atom, parameter);
            // Number
        else if (isNumber(parameter)) return parseNumber(parameter);
            // This
        else if (parameter.equals("this")) return parseThisOperator(atom);
            // Null
        else if (parameter.equals("null")) return new Factor("null", null);
            // Boolean
        else if (parameter.equals("true")) return new Factor("Boolean", new PBoolean(true));
            // Boolean
        else if (parameter.equals("false")) return new Factor("Boolean", new PBoolean(false));
        else {
            // Variable
            return new Factor(null, atom.getParent().getVariables(), parameter);
        }

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

    public Factor[] parseLocal(Atom atom) {
        String[] parametersSources = ParameterAssistant.split(atom.getSourceCode());
        return parse(atom, parametersSources);
    }

    public Factor parseConstructor(Atom atom, String s) {
        atom.setSourceCode(s);
        return new Factor(null, atom.getParent().getVariables(), new ConstructorParser().parse(atom));
    }

    public Factor parseThisOperator(Atom atom) {
        Block block = atom.getCurrent();
        while (block.hasParent()) {
            if (block instanceof VialBlock) {
                return new Factor(block.getName(), null);
            }
            block = block.getParent();
        }
        PandaException exception = new PandaException("ParameterParserException: Cannot use 'this' here", atom.getSourcesDivider());
        atom.getPandaParser().throwException(exception);
        return null;
    }

    public Factor parseArray(Atom atom, String s) {
        String array = s.substring(1, s.length() - 1);
        return new Factor("Array", new PArray(parse(atom, array)));
    }

    public Factor parseString(String s) {
        return new Factor("String", new PString(s.substring(1, s.length() - 1)));
    }

    public Factor parseNumber(String s) {
        return new Factor("Number", new PNumber(Integer.valueOf(s)));
    }

    public boolean isNumber(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
