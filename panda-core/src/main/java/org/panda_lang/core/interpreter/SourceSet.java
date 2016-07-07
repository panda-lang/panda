package org.panda_lang.core.interpreter;

import java.util.Collection;

/**
 * Set of {@link SourceFile} used by {@link Interpreter}
 */
public interface SourceSet {

    /**
     * @return collection of {@link SourceFile}
     */
    Collection<SourceFile> getSourceFiles();

}
