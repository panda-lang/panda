package org.panda_lang.panda.framework.language.interpreter.pattern.readers;

import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReader;

import java.util.ArrayList;
import java.util.Collection;

public class PandaExpressionReaders {

    private static final Collection<WildcardReader> READERS = new ArrayList<>();

    static {
        READERS.add(new ExpressionWildcardReader());
    }

    public static Collection<WildcardReader> getDefaults() {
        return new ArrayList<>(READERS);
    }

}
