package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.defaults;

import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReader;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultWildcardReaders {

    private static final Collection<WildcardReader> readers = new ArrayList<>();

    static {
        readers.add(new TypeReader());
    }

    public static Collection<WildcardReader> getDefaultReaders() {
        return new ArrayList<>(readers);
    }

}
