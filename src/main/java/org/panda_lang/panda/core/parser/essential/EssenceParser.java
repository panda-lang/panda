package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.lang.*;

public class EssenceParser implements Parser {

    @Override
    public Essence parse(Atom atom) {
        String parameter = atom.getSourceCode();

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
        // Null
        else if (parameter.equals("null")) {
            return new NullEssence();
        }
        // True
        else if (parameter.equals("true")) {
            return new BooleanEssence(true);
        }
        // False
        else if (parameter.equals("false")) {
            return new BooleanEssence(false);
        }

        return null;
    }

    public ArrayEssence parseArray(Atom atom, String s) {
        String array = s.substring(1, s.length() - 1);
        String[] parameters = array.split(",");
        Factor[] factors = new FactorParser().parse(atom, parameters);
        return new ArrayEssence(factors);
    }

    public StringEssence parseString(String s) {
        return new StringEssence(s.substring(1, s.length() - 1));
    }

    public Numeric parseNumber(String s) {
        char unit = s.toUpperCase().charAt(s.length() - 1);
        if (Character.isDigit(unit)) {
            return new IntEssence(Integer.parseInt(s));
        }

        NumberType numberType = NumberType.valueOf(unit);
        String numberValue = s.substring(0, s.length() - 1);

        switch (numberType) {
            case BYTE:
                return new ByteEssence(Byte.parseByte(numberValue));
            case SHORT:
                return new ShortEssence(Short.parseShort(numberValue));
            case INT:
                return new IntEssence(Integer.parseInt(numberValue));
            case LONG:
                return new LongEssence(Long.parseLong(numberValue));
            case FLOAT:
                return new FloatEssence(Float.parseFloat(numberValue));
            case DOUBLE:
                return new DoubleEssence(Double.parseDouble(numberValue));
            case FAT_PANDA:
                return null;
            default:
                System.out.print("Unknown number type " + s);
                return new IntEssence(0);
        }
    }

}
