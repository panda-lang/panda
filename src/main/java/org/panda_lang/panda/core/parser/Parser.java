package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.syntax.Executable;

public interface Parser {

    public Executable parse(Atom atom);

}
