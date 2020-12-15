/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.interpreter.parser;

import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.logging.Logger;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.pool.PoolService;
import org.panda_lang.language.interpreter.parser.stage.StageService;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.utilities.commons.function.Option;

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
