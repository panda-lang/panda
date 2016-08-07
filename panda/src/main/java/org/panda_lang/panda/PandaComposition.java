package org.panda_lang.panda;

import org.panda_lang.panda.composition.SyntaxComposition;

public class PandaComposition {

    private final SyntaxComposition syntaxComposition;

    public PandaComposition() {
        this.syntaxComposition = new SyntaxComposition();
    }

    public SyntaxComposition getSyntaxComposition() {
        return syntaxComposition;
    }

}
