package org.panda_lang.panda;

import org.panda_lang.core.interpreter.parser.ParserRepresentationRegistry;
import org.panda_lang.panda.lang.interpreter.util.PandaInterpreterConfiguration;

public class PandaFactory {

    public Panda createPanda() {
        Panda panda = new Panda();

        ParserRepresentationRegistry parserRepresentationRegistry = new ParserRepresentationRegistry();

        PandaComposition pandaComposition = panda.getPandaComposition();
        pandaComposition.setPandaInterpreterConfiguration(PandaInterpreterConfiguration.builder().build());

        return panda;
    }

}
