package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.PandaException;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Runtime;
import org.panda_lang.panda.core.syntax.block.VialBlock;
import org.panda_lang.panda.lang.*;

public class FactorParser implements Parser {

    @Override
    public Factor parse(Atom atom) {
        return parse(atom, atom.getSourceCode());
    }

    public Factor parse(Atom atom, String parameter) {
        if (FactorAssistant.isMath(parameter)) {
            // Math
            atom.getSourcesDivider().setLine(parameter);
            return new MathParser().parse(atom);
        }
        else if (FactorAssistant.isEquality(atom, parameter)) {
            // Equality
            atom.getSourcesDivider().setLine(parameter);
            return new EqualityParser().parse(atom);
        }
        else if (FactorAssistant.isMethod(atom, parameter)) {
            // Constructor
            if (parameter.startsWith("new") && parameter.charAt(3) == ' ') {
                return parseConstructor(atom, parameter);
            }
            // Method
            MethodParser parser = new MethodParser();
            atom.getSourcesDivider().setLine(parameter);
            Runtime methodRuntime = parser.parse(atom);
            return new Factor(methodRuntime);
        }

        char[] chars = parameter.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') {
            return parseString(parameter);
        }
        // Array
        else if (c == '[') {
            return parseArray(atom, parameter);
        }
        // Number
        else if (FactorAssistant.isNumber(parameter)) {
            return parseNumber(parameter);
        }
        // This
        else if (parameter.equals("this")) {
            return parseThisOperator(atom);
        }
        // Null
        else if (parameter.equals("null")) {
            return new Factor(new NullEssence());
        }
        // Boolean
        else if (parameter.equals("true")) {
            return new Factor(new BooleanEssence(true));
        }
        // Boolean
        else if (parameter.equals("false")) {
            return new Factor(new BooleanEssence(false));
        }
        else {
            // Variable
            return new Factor(parameter);
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
        String[] parametersSources = FactorAssistant.split(atom.getSourceCode());
        return parse(atom, parametersSources);
    }

    public Factor parseConstructor(Atom atom, String s) {
        atom.setSourceCode(s);
        return new Factor(new ConstructorParser().parse(atom));
    }

    public Factor parseArray(Atom atom, String s) {
        String array = s.substring(1, s.length() - 1);
        return new Factor(new ArrayEssence(parse(atom, array)));
    }

    public Factor parseString(String s) {
        return new Factor(new StringEssence(s.substring(1, s.length() - 1)));
    }

    public Factor parseThisOperator(Atom atom) {
        Block block = atom.getCurrent();
        while (block.hasParent()) {
            if (block instanceof VialBlock) {
                //#TODO: because I have no idea what it does...
                return new Factor(block.getName());
            }
            block = block.getParent();
        }
        PandaException exception = new PandaException("ParameterParserException: Cannot use 'this' here", atom.getSourcesDivider());
        atom.getPandaParser().throwException(exception);
        return null;
    }

    public Factor parseNumber(String s) {
        char unit = s.toUpperCase().charAt(s.length() - 1);
        if (Character.isDigit(unit)) {
            return new Factor(new IntEssence(Integer.parseInt(s)));
        }

        NumberType numberType = NumberType.valueOf(unit);
        String numberValue = s.substring(0, s.length() - 1);
        switch (numberType) {
            case BYTE:
                return new Factor(new ByteEssence(Byte.parseByte(numberValue)));
            case SHORT:
                return new Factor(new ShortEssence(Short.parseShort(numberValue)));
            case INT:
                return new Factor(new IntEssence(Integer.parseInt(numberValue)));
            case LONG:
                return new Factor(new LongEssence(Long.parseLong(numberValue)));
            case FLOAT:
                return new Factor(new FloatEssence(Float.parseFloat(numberValue)));
            case DOUBLE:
                return new Factor(new DoubleEssence(Double.parseDouble(numberValue)));
            case FAT_PANDA:
                return null;
            default:
                System.out.print("Unknown number type " + s);
                return new Factor(new IntEssence(0));
        }
    }

}
