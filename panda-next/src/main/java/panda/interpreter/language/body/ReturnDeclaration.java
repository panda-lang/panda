package panda.interpreter.language.body;

import org.objectweb.asm.Opcodes;
import panda.interpreter.compiler.Generator.InBodyGenerator;
import panda.interpreter.language.expression.ExpressionDeclaration;
import panda.interpreter.parser.Declaration.InBodyDeclaration;

public class ReturnDeclaration implements InBodyDeclaration {

    private final ExpressionDeclaration expressionDeclaration;

    public ReturnDeclaration(ExpressionDeclaration expressionDeclaration) {
        this.expressionDeclaration = expressionDeclaration;
    }

    @Override
    public InBodyGenerator getGenerator() {
        return (compiledScript, methodVisitor, variables) -> {
            expressionDeclaration.getGenerator().generate(compiledScript, methodVisitor, variables);
            methodVisitor.visitInsn(Opcodes.ARETURN);
        };
    }

}
