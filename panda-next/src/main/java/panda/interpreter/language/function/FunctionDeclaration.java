package panda.interpreter.language.function;

import org.jetbrains.annotations.Nullable;
import panda.interpreter.language.type.Signature;
import panda.interpreter.language.type.Signatures;
import panda.interpreter.language.body.Body;
import panda.interpreter.language.parameter.Parameter;
import panda.interpreter.parser.Declaration.InScriptDeclaration;
import panda.interpreter.parser.Declaration.InTypeDeclaration;
import panda.interpreter.parser.Scope;
import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration implements InScriptDeclaration, InTypeDeclaration {

    private final @Nullable Signature owner;
    private final String name;
    private final Signature returnType;
    private final List<Parameter> parameters = new ArrayList<>();
    private final Body<InBodyDeclaration> body = new Body<>();

    public FunctionDeclaration(@Nullable Signature owner, String name, Signature returnType) {
        this.owner = owner;
        this.name = name;
        this.returnType = returnType;
    }

    @Override
    public FunctionGenerator getGenerator() {
        return new FunctionGenerator(this);
    }

    @Override
    public Scope getScope() {
        return Scope.TYPE;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public Body<InBodyDeclaration> getBody() {
        return body;
    }

    public Signatures getSignatures() {
        return new Signatures(getParameters().stream().map(Parameter::getType).toList());
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Signature getReturnType() {
        return returnType;
    }

    public @Nullable Signature getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

}
