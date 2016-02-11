package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;

public class FileEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("File");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                StringEssence file = particle.getValueOfFactor(0);
                return new FileEssence(file.toString());
            }
        });
    }

    private final File file;

    public FileEssence(String file) {
        this.file = new File(file);
    }

    public File getFile() {
        return file;
    }

    public StringEssence getContentOfFile() {
        return new StringEssence(IOUtils.getContentOfFile(file));
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
