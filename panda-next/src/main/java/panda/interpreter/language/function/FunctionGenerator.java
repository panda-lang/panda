package panda.interpreter.language.function;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import panda.interpreter.compiler.CompiledScript;
import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.compiler.Generator.InTypeGenerator;
import java.util.HashMap;

public class FunctionGenerator implements InScriptGenerator, InTypeGenerator {

    private final FunctionDeclaration function;

    public FunctionGenerator(FunctionDeclaration function) {
        this.function = function;
    }

    @Override
    public void generate(CompiledScript compiledScript) {
        // function declared in script
    }

    @Override
    public void generate(CompiledScript compiledScript, ClassVisitor classWriter) {
        // function declared in type

        var methodVisitor = classWriter.visitMethod(
            Opcodes.ACC_PUBLIC,
            function.getName(),
            "(" + function.getSignatures().toJavaIdentifier() + ")" + function.getReturnType().toJavaIdentifier(),
            null,
            null
        );

        //            methodVisitor.visitFieldInsn(
        //                Opcodes.GETSTATIC,
        //                "java/lang/System",
        //                "out",
        //                "Ljava/io/PrintStream;"
        //            );
        //            methodVisitor.visitLdcInsn("hello");
        //            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
        //                "java/io/PrintStream",
        //                "println",
        //                "(Ljava/lang/String;)V",
        //                false
        //            );

        var variables = new HashMap<String, Integer>(); // name to index
        variables.put("this", 0);

        for (int index = 0; index < function.getParameters().size(); index++) {
            var parameter = function.getParameters().get(index);
            variables.put(parameter.getName(), index + 1);
        }

        for (var declaration : function.getBody().getStatements()) {
            declaration.getGenerator().generate(compiledScript, methodVisitor, variables);
        }

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

}
