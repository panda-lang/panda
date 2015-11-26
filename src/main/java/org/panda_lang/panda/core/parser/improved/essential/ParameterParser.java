package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.essential.assistant.ParameterAssistant;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.lang.PArray;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PString;

public class ParameterParser implements Parser {

    @Override
    public Parameter parse(Atom atom) {
        return parse(atom, atom.getSourceCode());
    }

    public Parameter parse(Atom atom, String parameter) {
        if (ParameterAssistant.isMath(parameter)) {
            // Math
            MathParser parser = new MathParser(parameter);
            return parser.parse(atom.getParent());
        } else if (ParameterAssistant.isMethod(atom, parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') return parseConstructor(atom, parameter);
            // Method
            MethodParser parser = new MethodParser();
            atom.setSourceCode(parameter);
            Method method = parser.parse(atom);
            return new Parameter(null, atom.getParent(), new Runtime(method));
        }

        char[] chars = parameter.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') return parseString(parameter);
            // Array
        else if (c == '[') return parseArray(atom, parameter);
            // Number
        else if (isNumber(parameter)) return parseNumber(parameter);
            // Null
        else if (parameter.equals("null")) return new Parameter("null", null);
            // Boolean
        else if (parameter.equals("true")) return new Parameter("Boolean", new PBoolean(true));
            // Boolean
        else if (parameter.equals("false")) return new Parameter("Boolean", new PBoolean(false));
        else {
            // Variable
            return new Parameter(null, atom.getParent(), parameter);
        }

    }

    public Parameter[] parse(Atom atom, String[] parametersSources) {
        Parameter[] parameters = new Parameter[parametersSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametersSources[i];
            parameters[i] = parse(atom, src);
        }
        return parameters;
    }

    public Parameter[] parseLocal(Atom atom) {
        String[] parametersSources = ParameterAssistant.split(atom.getSourceCode());
        Parameter[] parameters = new Parameter[parametersSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametersSources[i];
            parameters[i] = parse(atom, src);
        }
        return parameters;
    }

    public Parameter parseConstructor(Atom atom, String s) {
        atom.setSourceCode(s);
        return new Parameter(null, atom.getParent(), new ConstructorParser().parse(atom));
    }

    public Parameter parseArray(Atom atom, String s) {
        String array = s.substring(1, s.length() - 1);
        return new Parameter("Array", new PArray(parse(atom, array)));
    }

    public Parameter parseString(String s) {
        return new Parameter("String", new PString(s.substring(1, s.length() - 1)));
    }

    public Parameter parseNumber(String s) {
        return new Parameter("Number", new PNumber(Integer.valueOf(s)));
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
