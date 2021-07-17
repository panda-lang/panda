/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.parser;

import org.panda_lang.framework.architecture.Application;
import org.panda_lang.framework.architecture.Environment;
import org.panda_lang.framework.architecture.packages.Script;
import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.interpreter.parser.pool.PoolService;
import org.panda_lang.framework.interpreter.parser.stage.StageService;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.framework.interpreter.token.SourceStream;
import org.panda_lang.framework.interpreter.token.Streamable;
import panda.std.Option;

public class PandaContextCreator<T> implements ContextCreator<T> {

    protected final Option<Context<?>> parentContext;
    protected final Environment environment;
    protected final StageService stageService;
    protected final PoolService poolService;
    protected final ExpressionParser expressionParser;
    protected final TypeLoader typeLoader;
    protected final Application application;
    protected Script script;
    protected Snippet scriptSource;
    protected Imports imports;
    protected Scope scope;
    protected Snippet source;
    protected SourceStream stream;
    protected T subject;

    public PandaContextCreator(
            Option<Context<?>> parentContext,
            Environment environment,
            StageService stageService,
            PoolService poolService,
            ExpressionParser expressionParser,
            TypeLoader typeLoader,
            Application application) {

        this.parentContext = parentContext;
        this.environment = environment;
        this.stageService = stageService;
        this.poolService = poolService;
        this.expressionParser = expressionParser;
        this.typeLoader = typeLoader;
        this.application = application;
    }

    @Override
    public PandaContextCreator<T> fork(Context<T> context) {
        PandaContextCreator<T> fork = new PandaContextCreator<>(
                Option.of(context),
                environment,
                stageService,
                poolService,
                expressionParser,
                typeLoader,
                application);

        fork.script = script;
        fork.scriptSource = scriptSource;
        fork.imports = imports;
        fork.scope = scope;
        fork.source = source;
        fork.stream = stream;
        fork.subject = subject;

        return fork;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> ContextCreator<S> withSubject(S subject) {
        this.subject = (T) subject;
        return (ContextCreator<S>) this;
    }

    @Override
    public ContextCreator<T> withStream(Streamable stream) {
        this.stream = stream.toStream();
        return this;
    }

    @Override
    public ContextCreator<T> withSource(Snippetable source) {
        this.source = source.toSnippet();
        return this;
    }

    @Override
    public ContextCreator<T> withScope(Scope scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public ContextCreator<T> withImports(Imports imports) {
        this.imports = imports;
        return this;
    }

    @Override
    public ContextCreator<T> withScriptSource(Snippet scriptSource) {
        this.scriptSource = scriptSource;
        return this;
    }

    @Override
    public ContextCreator<T> withScript(Script script) {
        this.script = script;
        return this;
    }

    @Override
    public Context<T> toContext() {
        return new PandaContext<>(this);
    }

}
