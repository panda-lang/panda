package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationUnit;

public class PandaGenerationUnit implements GenerationUnit {

    private final GenerationCallback callback;
    private final ParserData delegated;

    public PandaGenerationUnit(GenerationCallback callback, ParserData delegated) {
        this.callback = callback;
        this.delegated = delegated;
    }

    @Override
    public ParserData getDelegated() {
        return delegated;
    }

    @Override
    public GenerationCallback getCallback() {
        return callback;
    }

}
