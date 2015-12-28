package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.*;
import org.panda_lang.panda.core.parser.essential.util.EssentialPriority;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class ImportParser implements Parser {

    static {
        ParserLayout parserLayout = new ParserLayout(new ImportParser());
        parserLayout.pattern("import *;", EssentialPriority.IMPORT.getPriority(), EssentialPriority.IMPORT.getPriority() * 10, PatternExtractor.FULL);
        ParserCenter.registerParser(parserLayout);
    }

    @Override
    public NamedExecutable parse(Atom atom) {
        return null;
    }

}
