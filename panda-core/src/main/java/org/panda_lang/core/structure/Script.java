package org.panda_lang.core.structure;

import org.panda_lang.core.interpreter.SourceFile;

public interface Script {

    /**
     * @return selected wrapper by the specified class and name
     */
    Wrapper select(Class<? extends Wrapper> clazz, String wrapperName);

    SourceFile getSourceFile();

    String getScriptName();

}
