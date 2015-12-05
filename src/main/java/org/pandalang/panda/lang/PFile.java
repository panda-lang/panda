package org.pandalang.panda.lang;

import org.pandalang.panda.core.scheme.ConstructorScheme;
import org.pandalang.panda.core.scheme.MethodScheme;
import org.pandalang.panda.core.scheme.ObjectScheme;
import org.pandalang.panda.core.syntax.Constructor;
import org.pandalang.panda.core.syntax.IExecutable;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.util.IOUtils;

import java.io.File;

public class PFile extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PFile.class, "File");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PFile>() {
            @Override
            public PFile run(Parameter... parameters) {
                return new PFile(parameters[0].getValue().toString());
            }
        }));
        // Method: create
        os.registerMethod(new MethodScheme("create", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PFile f = instance.getValue(PFile.class);
                File file = f.getFile();
                if (!file.exists()) {

                }
                return null;
            }
        }));
        // Method: isDirectory
        os.registerMethod(new MethodScheme("isDirectory", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PFile f = instance.getValue(PFile.class);
                return new PBoolean(f.getFile().isDirectory());
            }
        }));
        // Method: getContent
        os.registerMethod(new MethodScheme("getContent", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PFile f = instance.getValue(PFile.class);
                return f.getContent();
            }
        }));
    }

    private final File file;

    public PFile(String s) {
        this.file = new File(s);
    }

    public File getFile() {
        return file;
    }

    public PString getContent() {
        return new PString(IOUtils.getContent(file));
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
