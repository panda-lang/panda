package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowiredParameters {

    int detectFrom() default 0;

    int detectTo() default 0;

    Type[] value();

}
