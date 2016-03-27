package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.parser.util.match.parser.PatternExtractor;
import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.statement.Return;

public class ReturnParser implements Parser {

    public static void initialize(Panda panda) {
        ReturnParser returnParser = new ReturnParser();
        ParserLayout parserLayout = new ParserLayout(returnParser);
        parserLayout.pattern("return*;", EssentialPriority.RETURN.getPriority(), EssentialPriority.RETURN.getPriority() * 10, PatternExtractor.FULL);
        panda.getPandaCore().registerParser(parserLayout);
    }

    @Override
    public Return parse(Atom atom) {
        String line = atom.getSourcesDivider().getLine();
        String[] parts = line.split(" ", 2);

        Return returnElement = null;
        if (parts.length > 1) {
            FactorParser factorParser = new FactorParser();
            String factorSource = parts[1].substring(0, parts[1].length() - 1);
            Factor factor = factorParser.parse(atom, factorSource);
            returnElement = new Return(atom.getCurrent(), factor);
        }

        return returnElement;
    }

}
