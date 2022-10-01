package panda.interpreter.language.type;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import panda.interpreter.compiler.CompiledScript;
import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.language.function.FunctionDeclaration;

public class TypeGenerator implements InScriptGenerator {

    private final TypeDeclaration typeDeclaration;

    public TypeGenerator(TypeDeclaration typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public void generate(CompiledScript compiledScript) {
        var classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        classWriter.visit(
            Opcodes.V11,
            Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
            typeDeclaration.getTypeName(),
            null, // generic signature
            "java/lang/Object", // default super
            null
        );

        classWriter.visitSource(
            typeDeclaration.getTypeName() + "Panda.java",
            null // debug?
        );

        var constructorVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructorVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        constructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL,
            "java/lang/Object", // Object's constructor
            "<init>",
            "()V", // void
            false
        );
        constructorVisitor.visitInsn(Opcodes.RETURN);
        constructorVisitor.visitMaxs(1, 1);
        constructorVisitor.visitEnd();

        for (FunctionDeclaration function : typeDeclaration.getFunctions()) {
            function.getGenerator().generate(compiledScript, classWriter);
        }

        classWriter.visitEnd();
        compiledScript.addClass(classWriter.toByteArray());
    }

}