package org.panda_lang.language.interpreter.parser;

import org.panda_lang.language.architecture.Application;
import org.panda_lang.language.architecture.Environment;
import org.panda_lang.language.architecture.Script;
import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.pool.PoolService;
import org.panda_lang.language.interpreter.parser.stage.StageService;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.SourceStream;
import org.panda_lang.language.interpreter.token.Streamable;

public class PandaContextCreator<T> implements ContextCreator<T> {

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
            Environment environment,
            StageService stageService,
            PoolService poolService,
            ExpressionParser expressionParser,
            TypeLoader typeLoader,
            Application application) {

        this.environment = environment;
        this.stageService = stageService;
        this.poolService = poolService;
        this.expressionParser = expressionParser;
        this.typeLoader = typeLoader;
        this.application = application;
    }

    @Override
    public PandaContextCreator<T> fork() {
        PandaContextCreator<T> fork = new PandaContextCreator<>(
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
