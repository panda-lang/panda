package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.util.IOUtils;

import java.io.File;

public class PFile extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("File");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                PString file = particle.getValueOfFactor(0);
                return new PFile(file.toString());
            }
        });
    }

    private final File file;

    public PFile(String file) {
        super(vial);
        this.file = new File(file);
    }

    public File getFile() {
        return file;
    }

    public PString getContentOfFile() {
        return new PString(IOUtils.getContentOfFile(file));
    }

    @Override
    public String getType() {
        return "File";
    }

    @Override
    public String toString() {
        return file.getName();
    }

}
