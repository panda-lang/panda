package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.statement.Executable;

public interface Injection {

    void call(Atom atom, Executable executable);

}
