package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.statement.Executable;

public interface Injection {

    void call(ParserInfo parserInfo, Executable executable);

}
