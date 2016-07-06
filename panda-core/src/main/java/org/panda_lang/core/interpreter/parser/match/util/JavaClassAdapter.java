package org.panda_lang.core.interpreter.parser.match.util;

public interface JavaClassAdapter {

    Object call(Object instance, int methodID, Object[] parameters);

}
