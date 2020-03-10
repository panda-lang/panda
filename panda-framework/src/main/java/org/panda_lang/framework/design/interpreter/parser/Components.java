/*
 * Copyright (c) 2015-2020 Dzikoysk
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

import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.architecture.Script;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.interpreter.Interpretation;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.parser.generation.Generation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.framework.design.interpreter.source.SourceSet;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;

/**
 * The most common components available in the context
 */
public final class Components {

    /**
     * Represents the framework controller
     */
    public static final ContextComponent<FrameworkController> CONTROLLER = ContextComponent.of("controller", FrameworkController.class);

    /**
     * Represents the interpretation process
     */
    public static final ContextComponent<Interpretation> INTERPRETATION = ContextComponent.of("interpretation", Interpretation.class);

    /**
     * Represents the application environment
     */
    public static final ContextComponent<Environment> ENVIRONMENT = ContextComponent.of("environment", Environment.class);

    /**
     * Represents the generation
     */
    public static final ContextComponent<Generation> GENERATION = ContextComponent.of("generation", Generation.class);

    /**
     * Represents the pipeline path with all registered pipelines
     */
    public static final ContextComponent<PipelinePath> PIPELINE = ContextComponent.of("pipeline-path", PipelinePath.class);

    /**
     * Represents the main expression parser with all registered subparsers
     */
    public static final ContextComponent<ExpressionParser> EXPRESSION = ContextComponent.of("expression-parser", ExpressionParser.class);

    /**
     * Represents the application module loader
     */
    public static final ContextComponent<TypeLoader> TYPE_LOADER = ContextComponent.of("type-loader", TypeLoader.class);

    /**
     * Represents the current application
     */
    public static final ContextComponent<Application> APPLICATION = ContextComponent.of("application", Application.class);

    /**
     * Represents all sources to parse
     */
    public static final ContextComponent<SourceSet> SOURCES = ContextComponent.of("source-set", SourceSet.class);

    /**
     * Represents the current script
     */
    public static final ContextComponent<Script> SCRIPT = ContextComponent.of("script", Script.class);

    /**
     * Represents the current source of script
     */
    public static final ContextComponent<Snippet> SOURCE = ContextComponent.of("source", Snippet.class);

    /**
     * Represents imports assigned to the current script
     */
    public static final ContextComponent<Imports> IMPORTS = ContextComponent.of("imports", Imports.class);

    /**
     * Represents the currently parsed source
     */
    public static final ContextComponent<Snippet> CURRENT_SOURCE = ContextComponent.of("current-source", Snippet.class);

    /**
     * Represents the current stream of source
     */
    public static final ContextComponent<SourceStream> STREAM = ContextComponent.of("source-stream", SourceStream.class);

    /**
     * Represents the channel between handler and parser
     */
    public static final ContextComponent<Channel> CHANNEL = ContextComponent.of("channel", Channel.class);

    /**
     * Represents the current scope
     */
    public static final ContextComponent<Scope> SCOPE = ContextComponent.of("scope", Scope.class);

    private Components() { }

}
