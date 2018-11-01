package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.PipelineType;

import java.util.Arrays;

class GenerationTest {

    private static final PandaGeneration generation = new PandaGeneration();

    @BeforeAll
    public static void createPipelines() {
        generation.initialize(Arrays.asList(
                new PipelineType("b", 2.0),
                new PipelineType("a", 1.0),
                new PipelineType("c", 3.0))
        );
    }

    @Test
    public void testPipelineGeneration() throws Throwable {
        StringBuilder outputBuilder = new StringBuilder();

        generation.getPipeline("b").nextLayer().delegate((generation, nextLayer, delegatedData) -> outputBuilder.append("b "), null);
        generation.getPipeline("a").nextLayer().delegate((generation, nextLayer, delegatedData) -> outputBuilder.append("a "), null);
        generation.getPipeline("c").nextLayer().delegate((generation, nextLayer, delegatedData) -> outputBuilder.append("c "), null);

        generation.getPipeline("b").nextLayer().delegate((generation, nextLayer, delegatedData) -> {
            outputBuilder.append("b2 ");

            nextLayer.delegate((generation1, nextLayer1, delegatedData1) -> {
                generation.getPipeline("a").nextLayer().delegate((generation2, nextLayer2, delegatedData2) -> outputBuilder.append("a2 "), delegatedData1);
                outputBuilder.append("b3 ");
            }, delegatedData);
        }, null);

        generation.execute(null);
        Assertions.assertEquals("a b b2 b3 a2 c", outputBuilder.toString().trim());

        outputBuilder.setLength(0);
        generation.execute(null);
        Assertions.assertEquals("", outputBuilder.toString());
    }

}
