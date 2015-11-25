package org.panda_lang.panda.core.parser.improved.essential;

import org.panda_lang.panda.core.parser.improved.PandaParser;
import org.panda_lang.panda.core.parser.improved.Parser;
import org.panda_lang.panda.core.parser.improved.PatternExtractor;
import org.panda_lang.panda.core.parser.improved.SourcesDivider;
import org.panda_lang.panda.core.parser.improved.essential.assistant.ParameterAssistant;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.lang.PArray;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PString;

public class ParameterParser implements Parser {

    private PandaParser pandaParser;
    private SourcesDivider sourcesDivider;
    private PatternExtractor extractor;
    private Block parent;
    private Block previous;

    public ParameterParser(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous) {
        this.pandaParser = pandaParser;
        this.sourcesDivider = sourcesDivider;
        this.extractor = extractor;
        this.parent = parent;
        this.previous = previous;
    }

    public ParameterParser() {
    }

    @Override
    public Parameter parse(PandaParser pandaParser, SourcesDivider sourcesDivider, PatternExtractor extractor, Block parent, Block previous) {
        this.pandaParser = pandaParser;
        this.sourcesDivider = sourcesDivider;
        this.extractor = extractor;
        this.parent = parent;
        this.previous = previous;
        return parse(parent, new String(sourcesDivider.getSource()));
    }

    protected Parameter parse(Block block, String parameter) {
        if (ParameterAssistant.isMath(parameter)) {
            // Math
            MathParser parser = new MathParser(parameter);
            return parser.parse(block);
        } else if (ParameterAssistant.isMethod(parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') return parseConstructor(block, parameter);
            // Method
            MethodParser parser = new MethodParser();
            Method method = parser.parse(pandaParser, new SourcesDivider(parameter), extractor, parent, previous);
            return new Parameter(null, block, new Runtime(method));
        }

        char[] chars = parameter.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') return parseString(parameter);
            // Array
        else if (c == '[') return parseArray(block, parameter);
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
            return new Parameter(null, block, parameter);
        }

    }

    protected Parameter[] parse(Block parent, String[] parametersSources) {
        Parameter[] parameters = new Parameter[parametersSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametersSources[i];
            parameters[i] = parse(parent, src);
        }
        return parameters;
    }

    protected Parameter[] parseLocal() {
        String[] parametersSources = ParameterAssistant.split(new String(sourcesDivider.getSource()));
        Parameter[] parameters = new Parameter[parametersSources.length];
        for (int i = 0; i < parameters.length; i++) {
            String src = parametersSources[i];
            parameters[i] = parse(parent, src);
        }
        return parameters;
    }

    protected Parameter parseConstructor(Block block, String s) {
        return new Parameter(null, block, new ConstructorParser().parse(pandaParser, new SourcesDivider(s), extractor, parent, previous));
    }

    public Parameter parseArray(Block parent, String s) {
        String array = s.substring(1, s.length() - 1);
        return new Parameter("Array", new PArray(parse(parent, array)));
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

    @Override
    public Block getParent() {
        return parent;
    }

}
