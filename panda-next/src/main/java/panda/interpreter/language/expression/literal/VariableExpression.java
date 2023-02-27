package panda.interpreter.language.expression.literal;

import org.objectweb.asm.Opcodes;
import panda.interpreter.compiler.Generator.InBodyGenerator;
import panda.interpreter.language.expression.ExpressionDeclaration;
import panda.interpreter.language.type.Signature;

public class VariableExpression extends ExpressionDeclaration {

    private final String variableName;

    public VariableExpression(Signature returnType, String variableName) {
        super(returnType);
        this.variableName = variableName;
    }

    @Override
    public InBodyGenerator getGenerator() {
        return ((compiledScript, methodVisitor, variables) -> {
            var variableIndex = variables.get(variableName);
            methodVisitor.visitVarInsn(Opcodes.ALOAD, variableIndex);
        });
    }

}
