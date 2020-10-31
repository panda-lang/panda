package org.panda_lang.panda.language.resource.syntax.scope.block;

import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.ContextParser;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.collection.Component;

public abstract class BlockParser<R> implements ContextParser<Object, R> {

    protected static ScopeParser SCOPE_PARSER;

    @Override
    public Component<?>[] targets() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public void initialize(Context<?> context) {
        if (SCOPE_PARSER == null) {
            SCOPE_PARSER = new ScopeParser(context.getPoolService());
        }
    }

}
