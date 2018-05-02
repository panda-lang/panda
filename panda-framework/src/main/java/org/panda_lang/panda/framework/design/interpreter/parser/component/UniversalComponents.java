package org.panda_lang.panda.framework.design.interpreter.parser.component;

import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.interpreter.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;

public class UniversalComponents {

    public static final Component<Interpretation> INTERPRETATION = Component.of("interpretation", Interpretation.class);

    public static final Component<Application> APPLICATION = Component.of("application", Application.class);

    public static final Component<Script> SCRIPT = Component.of("script", Script.class);

    public static final Component<InterpreterFailure> FAILURE = Component.of("failure", InterpreterFailure.class);

    public static final Component<PipelineRegistry> PIPELINE = Component.of("pipeline-registry", PipelineRegistry.class);

    public static final Component<SourceStream> SOURCE_STREAM = Component.of("source-stream", SourceStream.class);

    public static final Component<CasualParserGeneration> GENERATION = Component.of("generation", CasualParserGeneration.class);

    public static final Component<ParserData> PARENT_DATA = Component.of("parent-data", ParserData.class);

}
