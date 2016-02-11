package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.assistant.FieldAssistant;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Field;
import org.panda_lang.panda.lang.NullEssence;

public class FieldParser implements Parser {

    @Override
    public Field parse(Atom atom) {
        String source = atom.getSourcesDivider().getLine();
        String[] ss = FieldAssistant.splitAndClear(source);
        if (ss == null || ss.length < 1) {
            System.out.println("[FieldParser] Cannot parseLocal: " + source);
            return null;
        }

        Factor factor = new Factor(new NullEssence());
        if (ss.length > 1) {
            atom.setSourceCode(ss[1]);
            FactorParser parser = new FactorParser();
            factor = parser.parse(atom);
        }

        String[] lss = ss[0].split(" ");
        String fieldName = lss.length > 1 ? lss[1] : lss[0];

        Field field = new Field(fieldName, factor);

        if (lss.length > 1) {
            field.setDataType(lss[0]);
        }

        return field;
    }

    public static void initialize(Panda panda) {
        ParserLayout parserLayout = new ParserLayout(new FieldParser());
        parserLayout.pattern("*=*;", EssentialPriority.VARIABLE.getPriority());
        parserLayout.pattern(";", EssentialPriority.VARIABLE.getPriority() * 100);
        panda.getPandaCore().registerParser(parserLayout);
    }

}
