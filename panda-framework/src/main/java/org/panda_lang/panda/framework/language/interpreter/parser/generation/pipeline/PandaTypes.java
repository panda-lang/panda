package org.panda_lang.panda.framework.language.interpreter.parser.generation.pipeline;

import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.PipelineType;

import java.util.Arrays;
import java.util.List;

public class PandaTypes {

    public static final String RAW_SYNTAX_LABEL = "RAW_SYNTAX";
    public static final PipelineType RAW_SYNTAX = new PipelineType(RAW_SYNTAX_LABEL, 1.0);

    public static final String TYPES_LABEL = "TYPES";
    public static final PipelineType TYPES = new PipelineType(TYPES_LABEL, 2.0);

    public static final String SYNTAX_LABEL = "SYNTAX";
    public static final PipelineType SYNTAX = new PipelineType(SYNTAX_LABEL, 3.0);

    public static final String CONTENT_LABEL = "CONTENT";
    public static final PipelineType CONTENT = new PipelineType(CONTENT_LABEL, 4.0);

    private static final PipelineType[] VALUES = new PipelineType[4];

    static {
        VALUES[0] = RAW_SYNTAX;
        VALUES[1] = TYPES;
        VALUES[2] = SYNTAX;
        VALUES[3] = CONTENT;
    }

    public static List<? extends PipelineType> getValues() {
        return Arrays.asList(VALUES);
    }

}
