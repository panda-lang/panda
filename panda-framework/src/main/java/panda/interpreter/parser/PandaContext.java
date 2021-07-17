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

package panda.interpreter.parser;

import panda.interpreter.architecture.Application;
import panda.interpreter.architecture.Environment;
import panda.interpreter.architecture.packages.Script;
import panda.interpreter.architecture.module.Imports;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.architecture.statement.Scope;
import panda.interpreter.logging.Logger;
import panda.interpreter.parser.expression.ExpressionParser;
import panda.interpreter.parser.pool.PoolService;
import panda.interpreter.parser.stage.StageService;
import panda.interpreter.source.Location;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;
import panda.std.Option;

// @formatter:off
public class PandaContext<T> implements Context<T> {

    protected final PandaContextCreator<T> creator;

    protected PandaContext(PandaContextCreator<T> creator) {
        this.creator = creator;
    }

    @Override
    public PandaContext<T> fork() {
        return new PandaContext<>(creator.fork(this));
    }

    @Override
    public ContextCreator<T> forkCreator() {
        return creator.fork(this);
    }

    @Override
    public Context<T> toContext() {
        return this;
    }

    @Override
    public Logger getLogger() {
        return getEnvironment().getLogger();
    }

    @Override
    public T getSubject() { return creator.subject; }

    @Override
    public SourceStream getStream() { return creator.stream; }

    @Override
    public Location toLocation() { return getSource().getLocation(); }

    @Override
    public Snippet getSource() { return creator.source; }

    @Override
    public Scope getScope() { return creator.scope; }

    @Override
    public Imports getImports() { return creator.imports; }

    @Override
    public Snippet getScriptSource() { return creator.scriptSource; }

    @Override
    public Script getScript() { return creator.script; }

    @Override
    public Application getApplication() { return creator.application; }

    @Override
    public TypeLoader getTypeLoader() { return creator.typeLoader; }

    @Override
    public ExpressionParser getExpressionParser() { return creator.expressionParser; }

    @Override
    public PoolService getPoolService() { return creator.poolService; }

    @Override
    public StageService getStageService() { return creator.stageService; }

    @Override
    public Environment getEnvironment() { return creator.environment; }

    @Override
    public Option<Context<?>> getParentContext() { return creator.parentContext; }

}
