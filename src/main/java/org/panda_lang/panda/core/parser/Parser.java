package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.syntax.NamedExecutable;

public interface Parser {

    NamedExecutable parse(Atom atom);

}
