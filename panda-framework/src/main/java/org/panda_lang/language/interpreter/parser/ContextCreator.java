package org.panda_lang.language.interpreter.parser;

import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.Streamable;

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
