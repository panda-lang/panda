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

import org.panda_lang.language.FrameworkController;
import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.logging.LoggerHolder;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.pool.ParserPoolService;
import org.panda_lang.language.interpreter.parser.stage.StageService;
import org.panda_lang.language.interpreter.source.SourceSet;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.SourceStream;

/**
 * Component based set of data used during the interpretation process
 */
public interface Context<T> extends Contextual<T>, LoggerHolder {

    /**
     * Clone context to a new independent instance
     */
    Context<T> fork();

    ContextCreator<T> forkCreator();

    T getSubject();

    SourceStream getStream();

    LocalChannel getChannel();

    Snippet getSource();

    Scope getScope();

    Imports getImports();

    Snippet getScriptSource();

    Script getScript();

    SourceSet getSourceSet();

    Application getApplication();

    TypeLoader getTypeLoader();

    ExpressionParser getExpressionParser();

    ParserPoolService getPoolService();

    StageService getStageService();

    Environment getEnvironment();

    FrameworkController getFrameworkController();

}
