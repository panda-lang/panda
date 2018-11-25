package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition.defaults;

import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition.WildcardConditionFactory;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultWildcardConditionFactories {

    private static final Collection<WildcardConditionFactory> factories = new ArrayList<>();

    static {
        factories.add(new TokenWildcardConditionFactory());
    }

    public static Collection<WildcardConditionFactory> getDefaultFactories() {
        return new ArrayList<>(factories);
    }

}
