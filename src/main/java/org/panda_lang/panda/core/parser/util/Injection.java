package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.parser.Atom;

public interface Injection {

    void call(Atom atom, Executable executable);

}
