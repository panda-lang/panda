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

import org.panda_lang.framework.architecture.packages.Script;
import org.panda_lang.framework.architecture.module.Imports;
import org.panda_lang.framework.architecture.statement.Scope;
import org.panda_lang.framework.interpreter.token.Snippet;
import org.panda_lang.framework.interpreter.token.Snippetable;
import org.panda_lang.framework.interpreter.token.Streamable;

public interface ContextCreator<T> extends Contextual<T> {

    ContextCreator<T> fork(Context<T> context);

    <S> ContextCreator<S> withSubject(S subject);

    ContextCreator<T> withStream(Streamable stream);

    ContextCreator<T> withSource(Snippetable source);

    ContextCreator<T> withScope(Scope scope);

    ContextCreator<T> withImports(Imports imports);

    ContextCreator<T> withScriptSource(Snippet scriptSource);

    ContextCreator<T> withScript(Script script);

}
