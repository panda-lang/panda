package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;

public class FileInst extends ObjectInst {

    static {
        Structure structure = new Structure("File");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                StringInst file = alice.getValueOfFactor(0);
                return new FileInst(file.toString());
            }
        });
    }

    private final File file;

    public FileInst(String file) {
        this.file = new File(file);
    }

    public File getFile() {
        return file;
    }

    public StringInst getContentOfFile() {
        return new StringInst(IOUtils.getContentOfFile(file));
    }

    @Override
    public Object getJavaValue() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

}
