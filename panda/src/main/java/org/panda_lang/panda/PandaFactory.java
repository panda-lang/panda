package org.panda_lang.panda;

import org.panda_lang.panda.lang.interpreter.util.PandaInterpreterConfiguration;
import org.panda_lang.panda.lang.registry.ParserRegistry;

public class PandaFactory {

    public Panda createPanda() {
        Panda panda = new Panda();

        PandaInterpreterConfiguration interpreterConfiguration = PandaInterpreterConfiguration.builder()
                .pipeline(ParserRegistry.getPipeline())
                .build();

        PandaComposition pandaComposition = panda.getPandaComposition();
        pandaComposition.setPandaInterpreterConfiguration(interpreterConfiguration);

        return panda;
    }

}
