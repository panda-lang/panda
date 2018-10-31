package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;

public class PandaGenerationPipeline implements GenerationPipeline {

    private final Generation generation;
    private GenerationLayer currentLayer;
    private GenerationLayer nextLayer;

    public PandaGenerationPipeline(Generation generation) {
        this.generation = generation;
        this.currentLayer = new PandaGenerationLayer(generation);
        this.nextLayer = new PandaGenerationLayer(generation);
    }

    @Override
    public void execute(ParserData data) throws Throwable {
        while (true) {
            currentLayer.callDelegates(this, data);

            if (nextLayer.countDelegates() == 0) {
                break;
            }

            currentLayer = nextLayer;
            nextLayer = new PandaGenerationLayer(generation);
        }
    }

    public GenerationLayer getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public GenerationLayer getNextLayer() {
        return nextLayer;
    }

}
