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

package org.panda_lang.framework.design.interpreter.parser;

import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.Interpretation;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.design.interpreter.source.SourceSet;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;

public final class Components {

    public static final ContextComponent<Interpretation> INTERPRETATION = ContextComponent.of("interpretation", Interpretation.class);

    public static final ContextComponent<Environment> ENVIRONMENT = ContextComponent.of("environment", Environment.class);

    public static final ContextComponent<Generation> GENERATION = ContextComponent.of("generation", Generation.class);

    public static final ContextComponent<PipelinePath> PIPELINE = ContextComponent.of("pipeline-path", PipelinePath.class);

    public static final ContextComponent<ExpressionParser> EXPRESSION = ContextComponent.of("expression-parser", ExpressionParser.class);

    public static final ContextComponent<ModuleLoader> MODULE_LOADER = ContextComponent.of("module-loader", ModuleLoader.class);

    public static final ContextComponent<Application> APPLICATION = ContextComponent.of("application", Application.class);

    public static final ContextComponent<SourceSet> SOURCES = ContextComponent.of("source-set", SourceSet.class);

    public static final ContextComponent<Script> SCRIPT = ContextComponent.of("script", Script.class);

    public static final ContextComponent<Snippet> SOURCE = ContextComponent.of("source", Snippet.class);

    public static final ContextComponent<Imports> IMPORTS = ContextComponent.of("imports", Imports.class);

    public static final ContextComponent<SourceStream> STREAM = ContextComponent.of("source-stream", SourceStream.class);

    public static final ContextComponent<Scope> SCOPE = ContextComponent.of("scope", Scope.class);

    private Components() { }

}
