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
import org.panda_lang.framework.architecture.Script;
import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.module.TypeLoader;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.interpreter.logging.LoggerHolder;
import org.panda_lang.framework.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.interpreter.parser.pool.PoolService;
import org.panda_lang.framework.interpreter.parser.stage.StageService;
import org.panda_lang.framework.interpreter.source.Localizable;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.SourceStream;
import org.panda_lang.utilities.commons.function.Option;

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
