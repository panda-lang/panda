package org.pandalang.panda.core.parser.improved;

import org.pandalang.panda.core.syntax.Executable;

public interface Parser {

    public Executable parse(Atom atom);

}
