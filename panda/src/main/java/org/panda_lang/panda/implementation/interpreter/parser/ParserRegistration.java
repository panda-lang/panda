package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.parser.UnifiedParser;
import org.panda_lang.core.interpreter.parser.ParserHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface ParserRegistration {

    Class<? extends UnifiedParser> parserClass();

    Class<? extends ParserHandler> handlerClass();

}


