package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;

public interface Executable {

    Inst execute(Alice alice);

}
