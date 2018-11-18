package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations;

public @interface AutowiredParameters {

    int detectFrom() default 0;

    int detectTo() default 0;

    Type[] value();

}
