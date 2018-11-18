package org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations;

import org.panda_lang.panda.utilities.commons.StringUtils;

import java.lang.annotation.Annotation;

public @interface Type {

    Class<? extends Annotation> with();

    int index() default -1;

    String value() default StringUtils.EMPTY;

}
