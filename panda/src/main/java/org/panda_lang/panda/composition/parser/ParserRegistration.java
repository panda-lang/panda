package org.panda_lang.panda.composition.parser;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;

public @interface ParserRegistration {

    Class<? extends Parser> parserClass();

    Class<? extends ParserHandler> handlerClass();

}

