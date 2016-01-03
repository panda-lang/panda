package org.panda_lang.panda.lang;

public class PFile extends PObject {
    /*
    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PFile.class, "File");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PFile>() {
            @Override
            public PFile run(Factor... factors) {
                return new PFile(factors[0].getValue().toString());
            }
        }));
        // Method: create
        os.registerMethod(new MethodScheme("create", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PFile f = instance.getValue(PFile.class);
                File file = f.getFile();
                if (!file.exists()) {

                }
                return null;
            }
        }));
        // Method: isDirectory
        os.registerMethod(new MethodScheme("isDirectory", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PFile f = instance.getValue(PFile.class);
                return new PBoolean(f.getFile().isDirectory());
            }
        }));
        // Method: getContentOfFile
        os.registerMethod(new MethodScheme("getContentOfFile", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PFile f = instance.getValue(PFile.class);
                return f.getContentOfFile();
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
*/
}
