/*
 * Copyright (c) 2015-2019 Dzikoysk
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
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserDebug;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;

public final class UniversalComponents {

    public static final Component<Interpretation> INTERPRETATION = Component.of("interpretation", Interpretation.class);

    public static final Component<Environment> ENVIRONMENT = Component.of("environment", Environment.class);

    public static final Component<Application> APPLICATION = Component.of("application", Application.class);

    public static final Component<Generation> GENERATION = Component.of("generation", Generation.class);

    public static final Component<PipelinePath> PIPELINE = Component.of("pipeline-path", PipelinePath.class);

    public static final Component<ModuleLoader> MODULE_LOADER = Component.of("module-loader", ModuleLoader.class);

    public static final Component<ExpressionParser> EXPRESSION = Component.of("expression-parser", ExpressionParser.class);



    public static final Component<Script> SCRIPT = Component.of("script", Script.class);

    public static final Component<Snippet> SOURCE = Component.of("source", Snippet.class);

    public static final Component<SourceStream> SOURCE_STREAM = Component.of("source-stream", SourceStream.class);

    public static final Component<ScopeLinker> SCOPE_LINKER = Component.of("panda-scope-linker", ScopeLinker.class);



    public static final Component<ParserDebug> PARSER_DEBUG = Component.of("parser-debug", ParserDebug.class);

    public static final Component<ParserData> PARENT_DATA = Component.of("parent-data", ParserData.class);

}
