package org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations;

import org.panda_lang.panda.utilities.commons.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {

    Class<? extends Annotation> with();

    int index() default -1;

    String value() default StringUtils.EMPTY;

    boolean nullable() default false;

}
