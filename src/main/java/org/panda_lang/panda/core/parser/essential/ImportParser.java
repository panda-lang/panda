package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.Parser;
import org.panda_lang.panda.core.parser.ParserLayout;
import org.panda_lang.panda.core.parser.PatternExtractor;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.Import;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class ImportParser implements Parser {

    @Override
    public NamedExecutable parse(Atom atom) {
        final String source = atom.getSourcesDivider().getLine();
        final StringBuilder importBuilder = new StringBuilder();

        int stage = 0;
        Import importElement = null;
        String operator = null;

        for (char c : source.toCharArray()) {
            if (Character.isWhitespace(c) || c == ';') {
                switch (stage) {
                    case 0:
                        stage = 1;
                        break;
                    case 1:
                        importElement = new Import(importBuilder.toString());
                        importBuilder.setLength(0);
                        stage = 2;
                        break;
                    case 2:
                        operator = importBuilder.toString();
                        importBuilder.setLength(0);
                        stage = 3;
                        break;
                    case 3:
                        if (operator.equals(">")) {
                            String specific = importBuilder.toString();
                            importElement.setSpecific(specific);
                        }
                        else if (operator.equals("as")) {
                            String as = importBuilder.toString();
                            importElement.setAs(as);
                        }
                        importBuilder.setLength(0);
                        stage = 2;
                        break;
                    default:
                        break;
                }
            }
            else if (stage != 0) {
                importBuilder.append(c);
            }
        }

        atom.getPandaParser().getDependencies().importElement(importElement);
        return importElement;
    }

    public static void initialize(Panda panda) {
        ParserLayout parserLayout = new ParserLayout(new ImportParser());
        parserLayout.pattern("import *;", EssentialPriority.IMPORT.getPriority(), EssentialPriority.IMPORT.getPriority() * 10, PatternExtractor.FULL);
        panda.getPandaCore().registerParser(parserLayout);
    }

}
