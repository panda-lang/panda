package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.GenerationPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.types.PipelineType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PandaGeneration implements Generation {

    private final Map<String, GenerationPipeline> pipelines = new LinkedHashMap<>();

    public void initialize(List<PipelineType> types) {
        Collections.sort(types);

        for (PipelineType type : types) {
            pipelines.put(type.getName(), new PandaGenerationPipeline(this));
        }
    }

    @Override
    public void execute(ParserData data) throws Throwable {
        for (GenerationPipeline pipeline : pipelines.values()) {
            pipeline.execute(data);
        }
    }

    @Override
    public GenerationPipeline getPipeline(String name) {
        return pipelines.get(name);
    }

}
