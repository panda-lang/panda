package org.panda_lang.panda.language.resource.syntax.scope.block;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;

public abstract class BlockParser<R> implements ContextParser<Object, R> {

    protected static ScopeParser SCOPE_PARSER;

    @Override
    public void initialize(Context<Object> context) {
        if (SCOPE_PARSER == null) {
            SCOPE_PARSER = new ScopeParser(context.getPoolService());
        }
    }

}
