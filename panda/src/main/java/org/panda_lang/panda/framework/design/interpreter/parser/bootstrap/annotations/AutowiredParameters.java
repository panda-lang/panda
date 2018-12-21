package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowiredParameters {

    int skip() default 0;

    int to() default -1;

    Type[] value();

}
