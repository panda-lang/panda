package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.assistant.FieldAssistant;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Field;
import org.panda_lang.panda.lang.NullEssence;

public class FieldParser implements Parser {

    public static void initialize(Panda panda) {
        ParserLayout parserLayout = new ParserLayout(new FieldParser());
        parserLayout.pattern("*=*;", EssentialPriority.VARIABLE.getPriority());
        parserLayout.pattern(";", EssentialPriority.VARIABLE.getPriority() * 100);
        panda.getPandaCore().registerParser(parserLayout);
    }

    @Override
    public Field parse(ParserInfo parserInfo) {
        String source = parserInfo.getSourcesDivider().getLine();
        String[] ss = FieldAssistant.splitAndClear(source);
        if (ss == null || ss.length < 1) {
            System.out.println("[FieldParser] Cannot splitAndParse: " + source);
            return null;
        }

        RuntimeValue runtimeValue = new RuntimeValue(new NullEssence());
        if (ss.length > 1) {
            parserInfo.setSourceCode(ss[1]);
            FactorParser parser = new FactorParser();
            runtimeValue = parser.parse(parserInfo);
        }

        String[] lss = ss[0].split(" ");
        String fieldName = lss.length > 1 ? lss[1] : lss[0];

        Field field = new Field(fieldName, runtimeValue);

        if (lss.length > 1) {
            field.setDataType(lss[0]);
            runtimeValue.setDataType(lss[0]);
        }

        return field;
    }

}
