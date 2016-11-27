package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface ParserRegistration {

    Class<? extends UnifiedParser> parserClass();

    Class<? extends ParserHandler> handlerClass();

    int priority() default 0;

}


