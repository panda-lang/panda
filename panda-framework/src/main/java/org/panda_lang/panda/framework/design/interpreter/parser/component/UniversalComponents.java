/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.design.interpreter.parser.component;

import org.panda_lang.panda.framework.design.architecture.Application;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public class UniversalComponents {

    public static final Component<Interpretation> INTERPRETATION = Component.of("interpretation", Interpretation.class);

    public static final Component<Application> APPLICATION = Component.of("application", Application.class);

    public static final Component<Script> SCRIPT = Component.of("script", Script.class);

    public static final Component<PipelineRegistry> PIPELINE = Component.of("pipeline-registry", PipelineRegistry.class);

    public static final Component<TokenizedSource> SOURCE = Component.of("source", TokenizedSource.class);

    public static final Component<SourceStream> SOURCE_STREAM = Component.of("source-stream", SourceStream.class);

    public static final Component<CasualParserGeneration> GENERATION = Component.of("generation", CasualParserGeneration.class);

    public static final Component<ParserData> PARENT_DATA = Component.of("parent-data", ParserData.class);

}
