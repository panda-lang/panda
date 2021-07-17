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
import panda.interpreter.logging.LoggerHolder;
import panda.interpreter.parser.expression.ExpressionParser;
import panda.interpreter.parser.pool.PoolService;
import panda.interpreter.parser.stage.StageService;
import panda.interpreter.source.Localizable;
import panda.interpreter.token.Snippet;
import panda.interpreter.token.SourceStream;
import panda.std.Option;

/**
 * Component based set of data used during the interpretation process
 */
public interface Context<T> extends Contextual<T>, Localizable, LoggerHolder {

    /**
     * Clone context to a new independent instance
     */
    Context<T> fork();

    ContextCreator<T> forkCreator();

    T getSubject();

    SourceStream getStream();

    Snippet getSource();

    Scope getScope();

    Imports getImports();

    Snippet getScriptSource();

    Script getScript();

    Application getApplication();

    TypeLoader getTypeLoader();

    ExpressionParser getExpressionParser();

    PoolService getPoolService();

    StageService getStageService();

    Environment getEnvironment();

    Option<Context<?>> getParentContext();

}
