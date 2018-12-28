package org.panda_lang.panda.framework.language.interpreter.parser.scope.block;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;

public interface BlockSubparser extends Parser, ParserHandler {

    BlockData parse(ParserData data, Tokens declaration) throws Throwable;

}
