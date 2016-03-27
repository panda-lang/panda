package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.statement.Block;

public interface BlockInitializer {

    Block initialize(Atom atom);

}
