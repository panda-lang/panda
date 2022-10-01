package panda.interpreter.language.function;

import panda.interpreter.compiler.Generator.InTypeGenerator;
import panda.interpreter.language.type.Signature;
import panda.interpreter.language.type.Signatures;
import panda.interpreter.language.body.Body;
import panda.interpreter.language.parameter.Parameter;
import panda.interpreter.parser.Declaration.InTypeDeclaration;
import panda.interpreter.parser.Scope;
import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration implements InTypeDeclaration {

    private final String name;
    private final Signature returnType;
    private final List<Parameter> parameters = new ArrayList<>();
    private final Body<InBodyDeclaration> body;

    public FunctionDeclaration(String name, Signature returnType, Body body) {
        this.name = name;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public InTypeGenerator getGenerator() {
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

    public String getName() {
        return name;
    }

}
