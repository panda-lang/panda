package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.PipelineType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PandaGeneration implements Generation {

    private final Map<String, GenerationPipeline> pipelines = new LinkedHashMap<>();
    private GenerationPipeline currentPipeline;

    public void initialize(List<? extends PipelineType> types) {
        Collections.sort(types);

        for (PipelineType type : types) {
            pipelines.put(type.getName(), new PandaGenerationPipeline(this, type.getName()));
        }
    }

    @Override
    public void execute(ParserData data) throws Throwable {
        while (countDelegates(null) > 0) {
            executeOnce(data);
        }
    }

    private void executeOnce(ParserData data) throws Throwable {
        for (GenerationPipeline pipeline : pipelines.values()) {
            // System.out.println("Called " + pipeline.name());
            currentPipeline = pipeline;

            if (!pipeline.execute(data)) {
                break;
            }
        }

        currentPipeline = null;
    }

    @Override
    public int countDelegates(@Nullable GenerationPipeline toPipeline) {
        int count = 0;

        for (GenerationPipeline pipeline : pipelines.values()) {
            count += pipeline.countDelegates();

            if (pipeline.equals(toPipeline)) {
                break;
            }
        }

        return count;
    }

    @Override
    public @Nullable GenerationPipeline currentPipeline() {
        return currentPipeline;
    }

    @Override
    public GenerationPipeline pipeline(PipelineType type) {
        return pipeline(type.getName());
    }

    @Override
    public GenerationPipeline pipeline(String name) {
        return pipelines.get(name);
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder("Panda Generation { ");

        for (GenerationPipeline pipeline : pipelines.values()) {
            if (pipeline.countDelegates() == 0) {
                continue;
            }

            message.append(pipeline.toString()).append(", ");
        }

        message.setLength(message.length() - 2);
        return message.append(" }").toString();
    }

}
