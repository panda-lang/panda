package org.panda_lang.panda.lang.interpreter;

import org.panda_lang.core.interpreter.SourceFile;
import org.panda_lang.core.util.IOUtils;

import javax.xml.transform.Source;
import java.io.File;

public class PandaSourceFile implements SourceFile {

    private final File file;

    public PandaSourceFile(File file) {
        this.file = file;
    }

    @Override
    public String getContent() {
        return IOUtils.getContentOfFile(file);
    }

    @Override
    public File getFile() {
        return file;
    }

}
