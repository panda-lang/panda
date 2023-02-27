package panda.interpreter.language.type;

import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.language.function.FunctionDeclaration;
import panda.interpreter.parser.Declaration.InScriptDeclaration;
import panda.interpreter.parser.Scope;
import java.util.ArrayList;
import java.util.List;

public class TypeDeclaration implements InScriptDeclaration {

    private final String typeName;
    private final List<Object> fields = new ArrayList<>();
    private final List<FunctionDeclaration> functions = new ArrayList<>();

    public TypeDeclaration(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public InScriptGenerator getGenerator() {
        return new TypeGenerator(this);
    }

    @Override
    public Scope getScope() {
        return Scope.SCRIPT;
    }

    public void addFunction(FunctionDeclaration functionStatement) {
        this.functions.add(functionStatement);
    }

    public List<FunctionDeclaration> getFunctions() {
        return functions;
    }

    public String getTypeName() {
        return typeName;
    }

}
