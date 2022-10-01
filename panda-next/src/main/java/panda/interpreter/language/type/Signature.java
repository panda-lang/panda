package panda.interpreter.language.type;

public class Signature {

    private final String subject;
    private final boolean isAbstract;
    private final Signatures generics;

    public Signature(String subject, boolean isAbstract, Signatures generics) {
        this.subject = subject;
        this.isAbstract = isAbstract;
        this.generics = generics;
    }

    public Signature(String subject) {
        this(subject, false, new Signatures());
    }

    public String toJavaIdentifier() {
        return "L" + subject + ";";
    }

    public Signatures getGenerics() {
        return generics;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getSubject() {
        return subject;
    }

}
