package org.panda_lang.panda;

import org.panda_lang.panda.composition.parser.ParserComposition;
import org.panda_lang.panda.composition.syntax.SyntaxComposition;

public class PandaComposition {

    private final SyntaxComposition syntaxComposition;
    private final ParserComposition parserComposition;

    public PandaComposition() {
        this.syntaxComposition = new SyntaxComposition();
        this.parserComposition = new ParserComposition();
    }

    public ParserComposition getParserComposition() {
        return parserComposition;
    }

    public SyntaxComposition getSyntaxComposition() {
        return syntaxComposition;
    }

}
