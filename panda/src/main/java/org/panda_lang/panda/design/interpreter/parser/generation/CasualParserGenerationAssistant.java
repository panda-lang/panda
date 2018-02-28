package org.panda_lang.panda.design.interpreter.parser.generation;

import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;

public class CasualParserGenerationAssistant {

    public static void delegateImmediately(ParserInfo info, CasualParserGenerationCallback callback) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);
        generation.getLayer(CasualParserGenerationType.HIGHER).delegateImmediately(callback, info.fork());
    }

}
