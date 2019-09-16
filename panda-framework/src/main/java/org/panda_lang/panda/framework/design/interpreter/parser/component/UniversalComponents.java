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
import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.interpreter.Interpretation;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.source.SourceSet;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.SourceStream;

public final class UniversalComponents {

    public static final Component<Interpretation> INTERPRETATION = Component.of("interpretation", Interpretation.class);

    public static final Component<Environment> ENVIRONMENT = Component.of("environment", Environment.class);

    public static final Component<Application> APPLICATION = Component.of("application", Application.class);

    public static final Component<Generation> GENERATION = Component.of("generation", Generation.class);

    public static final Component<PipelinePath> PIPELINE = Component.of("pipeline-path", PipelinePath.class);

    public static final Component<ModuleLoader> MODULE_LOADER = Component.of("module-loader", ModuleLoader.class);

    public static final Component<ExpressionParser> EXPRESSION = Component.of("expression-parser", ExpressionParser.class);


    public static final Component<SourceSet> SOURCES = Component.of("source-set", SourceSet.class);

    public static final Component<Script> SCRIPT = Component.of("script", Script.class);

    public static final Component<Snippet> SOURCE = Component.of("source", Snippet.class);

    public static final Component<SourceStream> STREAM = Component.of("source-stream", SourceStream.class);

    public static final Component<Scope> SCOPE = Component.of("scope", Scope.class);


    public static final Component<Context> PARENT_DATA = Component.of("parent-data", Context.class);

}
