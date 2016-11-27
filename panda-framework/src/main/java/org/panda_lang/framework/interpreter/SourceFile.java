package org.panda_lang.framework.interpreter;

import java.io.File;

/**
 * SourceFiles are used by {@link SourceSet} and {@link Interpreter}
 */
public interface SourceFile {

    /**
     * @return content of file
     */
    String getContent();

    /**
     * @return regular {@link File}
     */
    File getFile();

}
