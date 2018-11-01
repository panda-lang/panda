package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline.types.PipelineType;

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
        generation.getPipeline("b").getNextLayer().delegate((generation, nextLayer, delegatedData) -> System.out.println("b"), null);
        generation.getPipeline("a").getNextLayer().delegate((generation, nextLayer, delegatedData) -> System.out.println("a"), null);
        generation.getPipeline("c").getNextLayer().delegate((generation, nextLayer, delegatedData) -> System.out.println("c"), null);

        generation.getPipeline("b").getNextLayer().delegate((generation, nextLayer, delegatedData) -> {
            System.out.println("b2");

            nextLayer.delegate((generation1, nextLayer1, delegatedData1) -> {
                System.out.println("b3");

                generation.getPipeline("a").getNextLayer().delegate((generation2, nextLayer2, delegatedData2) -> {
                    System.out.println("a2");
                }, delegatedData1);
            }, delegatedData);
        }, null);

        generation.execute(null);
        System.out.println("-");
        generation.execute(null);
    }

}
