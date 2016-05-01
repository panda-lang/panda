package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.assistant.FactorAssistant;
import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.lang.*;

public class EssenceParser implements Parser {

    @Override
    public RuntimeValue parse(ParserInfo parserInfo) {
        Inst inst = parse(parserInfo, parserInfo.getSourceCode());
        return inst != null ? new RuntimeValue(inst) : null;
    }

    public Inst parse(ParserInfo parserInfo, String source) {
        char[] chars = source.toCharArray();
        char c = chars[0];

        // String
        if (c == '"') {
            return parseString(source);
        }
        // Array
        else if (c == '[') {
            return parseArray(parserInfo, source);
        }
        // Number
        else if (FactorAssistant.isNumber(source)) {
            return parseNumber(source);
        }
        // Null
        else if (source.equals("null")) {
            return new NullInst();
        }
        // True
        else if (source.equals("true")) {
            return new BooleanInst(true);
        }
        // False
        else if (source.equals("false")) {
            return new BooleanInst(false);
        }

        return null;
    }

    public ArrayInst parseArray(ParserInfo parserInfo, String s) {
        String array = s.substring(1, s.length() - 1);
        String[] parameters = array.split(",");
        RuntimeValue[] runtimeValues = new FactorParser().parse(parserInfo, parameters);
        return new ArrayInst(runtimeValues);
    }

    public StringInst parseString(String s) {
        return new StringInst(s.substring(1, s.length() - 1));
    }

    public Numeric parseNumber(String s) {
        char unit = s.toUpperCase().charAt(s.length() - 1);
        if (Character.isDigit(unit)) {
            return new IntInst(Integer.parseInt(s));
        }

        NumberType numberType = NumberType.valueOf(unit);
        String numberValue = s.substring(0, s.length() - 1);

        switch (numberType) {
            case BYTE:
                return new ByteInst(Byte.parseByte(numberValue));
            case SHORT:
                return new ShortInst(Short.parseShort(numberValue));
            case INT:
                return new IntInst(Integer.parseInt(numberValue));
            case LONG:
                return new LongInst(Long.parseLong(numberValue));
            case FLOAT:
                return new FloatInst(Float.parseFloat(numberValue));
            case DOUBLE:
                return new DoubleInst(Double.parseDouble(numberValue));
            case FAT_PANDA:
                return null;
            default:
                System.out.print("Unknown number type " + s);
                return new IntInst(0);
        }
    }

}
