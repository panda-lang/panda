package panda.interpreter.compiler;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import java.util.HashMap;

public interface Generator {

    @FunctionalInterface
    interface InScriptGenerator extends Generator {
        void generate(CompiledScript compiledScript);
    }

    @FunctionalInterface
    interface InTypeGenerator extends Generator {
        void generate(CompiledScript compiledScript, ClassVisitor classVisitor);
    }

    @FunctionalInterface
    interface InBodyGenerator extends Generator {
        void generate(CompiledScript compiledScript, MethodVisitor methodVisitor, HashMap<String, Integer> variables);
    }

}
