package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.statement.Block;

public interface BlockInitializer {

    Block initialize(ParserInfo parserInfo);

}
