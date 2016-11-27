package org.panda_lang.panda.implementation.interpreter;

import org.panda_lang.framework.interpreter.SourceFile;
import org.panda_lang.framework.interpreter.SourceSet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class PandaSourceSet implements SourceSet {

    private final Collection<SourceFile> sourceFiles;

    public PandaSourceSet() {
        this.sourceFiles = new ArrayList<>();
    }

    public SourceFile add(File file) {
        SourceFile sourceFile = new PandaSourceFile(file);
        sourceFiles.add(sourceFile);
        return sourceFile;
    }

    @Override
    public Collection<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

}
