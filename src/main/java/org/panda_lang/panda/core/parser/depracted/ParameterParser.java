package org.panda_lang.panda.core.parser.depracted;

import org.panda_lang.panda.core.parser.depracted.util.ParameterParserUtils;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.lang.PArray;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PString;

public class ParameterParser {

    private String source;

    public ParameterParser() {
    }

    public ParameterParser(String source) {
        this.source = source;
    }

    public Parameter parse(Block block, String param) {
        if (ParameterParserUtils.isMath(param)) {
            // Math
            MathParser parser = new MathParser(param);
            return parser.parse(block);
        } else if (ParameterParserUtils.isMethod(param)) {
            // Constructor
            if (param.startsWith("new") && param.charAt(3) == ' ') return parseConstructor(block, param);
            // Method
            MethodParser parser = new MethodParser(PandaParser.getCurrentInstance().getScript(), block, param);
            Method method = parser.parse();
            return new Parameter(null, block, new Runtime(method));
        }

        char[] chars = param.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') return parseString(param);
            // Array
        else if (c == '[') return parseArray(block, param);
            // Number
        else if (isNumber(param)) return parseNumber(param);
            // Null
        else if (param.equals("null")) return new Parameter("null", null);
            // Boolean
        else if (param.equals("true")) return new Parameter("Boolean", new PBoolean(true));
            // Boolean
        else if (param.equals("false")) return new Parameter("Boolean", new PBoolean(false));
        else {
            // Variable
            return new Parameter(null, block, param);
        }

    }

    public Parameter[] parse(Block block, String[] parametrsSources) {
        Parameter[] parameters = new Parameter[parametrsSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametrsSources[i];
            parameters[i] = parse(block, src);
        }
        return parameters;
    }

    public Parameter[] parse(Block block) {
        String[] parametrsSources = ParameterParserUtils.split(source);
        Parameter[] parameters = new Parameter[parametrsSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametrsSources[i];
            parameters[i] = parse(block, src);
        }
        return parameters;
    }

    public Parameter parseConstructor(Block block, String s) {
        return new Parameter(null, block, new ConstructorParser(s).parse(block));
    }

    public Parameter parseArray(Block block, String s) {
        ParameterParser parser = new ParameterParser(s.substring(1, s.length() - 1));
        return new Parameter("Array", new PArray(parser.parse(block)));
    }

    public Parameter parseString(String s) {
        return new Parameter("String", new PString(s.substring(1, s.length() - 1)));
    }

    public Parameter parseNumber(String s) {
        return new Parameter("Number", new PNumber(Integer.valueOf(s)));
    }

    public boolean isNumber(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

}
