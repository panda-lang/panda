package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.syntax.NamedExecutable;

public interface Parser
{

    public NamedExecutable parse(Atom atom);

}
