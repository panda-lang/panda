package panda.interpreter.language.expression.literal;

import panda.interpreter.compiler.Generator.InBodyGenerator;
import panda.interpreter.language.expression.ExpressionDeclaration;
import panda.interpreter.language.type.Signature;

public class StringExpression extends ExpressionDeclaration {

    private final String value;

    public StringExpression(String value) {
        super(new Signature("java/lang/String"));
        this.value = value;
    }

    @Override
    public InBodyGenerator getGenerator() {
        return ((compiledScript, methodVisitor, variables) -> {
            methodVisitor.visitLdcInsn(value);
        });
    }

}
