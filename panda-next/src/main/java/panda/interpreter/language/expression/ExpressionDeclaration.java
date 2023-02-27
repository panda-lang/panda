package panda.interpreter.language.expression;

import panda.interpreter.language.type.Signature;
import panda.interpreter.parser.Declaration.InBodyDeclaration;
import panda.interpreter.parser.Scope;

public abstract class ExpressionDeclaration implements InBodyDeclaration {

    private final Signature returnType;

    public ExpressionDeclaration(Signature returnType) {
        this.returnType = returnType;
    }

    public Signature getReturnType() {
        return returnType;
    }

    @Override
    public Scope getScope() {
        return Scope.BODY;
    }

}
