package panda.interpreter.language.type;

import panda.utilities.text.Joiner;
import java.util.ArrayList;
import java.util.List;

public class Signatures {

    private final List<Signature> signatures;

    public Signatures() {
        this.signatures = new ArrayList<>();
    }

    public Signatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    public String toJavaIdentifier() {
        return Joiner.on(";")
            .join(signatures, Signature::toJavaIdentifier)
            .toString();
    }

    public List<Signature> getSignatures() {
        return signatures;
    }

    public static String toString(Signature[] signatures) {
        return Joiner.on(";")
            .join(signatures, Signature::toJavaIdentifier)
            .toString();
    }

}
