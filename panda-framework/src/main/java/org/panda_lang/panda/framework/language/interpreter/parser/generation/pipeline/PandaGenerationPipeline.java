package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;

public class PandaGenerationPipeline implements GenerationPipeline {

    private final String name;
    private final Generation generation;
    private GenerationLayer currentLayer;
    private GenerationLayer nextLayer;

    public PandaGenerationPipeline(Generation generation, String name) {
        this.name = name;
        this.generation = generation;
        this.currentLayer = new PandaGenerationLayer(this);
        this.nextLayer = new PandaGenerationLayer(this);
    }

    @Override
    public boolean execute(ParserData data) throws Throwable {
        while (true) {
            currentLayer.callDelegates(this, data);

            if (nextLayer.countDelegates() == 0) {
                break;
            }

            currentLayer = nextLayer;
            nextLayer = new PandaGenerationLayer(this);

            if (generation.countDelegates(this) > 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int countDelegates() {
        return currentLayer.countDelegates() + nextLayer.countDelegates();
    }

    @Override
    public GenerationLayer currentLayer() {
        return currentLayer;
    }

    @Override
    public GenerationLayer nextLayer() {
        return nextLayer;
    }

    @Override
    public Generation generation() {
        return generation;
    }

    @Override
    public String name() {
        return name;
    }

}
