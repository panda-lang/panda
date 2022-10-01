package panda.interpreter.language.body;

import panda.interpreter.parser.Declaration;
import java.util.ArrayList;
import java.util.List;

public class Body<D extends Declaration<?>> {

    private final List<D> declarations = new ArrayList<>();

    public void addStatement(D declaration) {
        declarations.add(declaration);
    }

    public List<? extends D> getStatements() {
        return declarations;
    }

}
