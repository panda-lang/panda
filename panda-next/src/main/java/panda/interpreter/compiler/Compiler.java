package panda.interpreter.compiler;

import panda.interpreter.parser.AbstractSyntaxTree;

public class Compiler {

    public CompiledScript compile(AbstractSyntaxTree ast) {
        var compiledScript = new CompiledScript();

        for (var declaration : ast.getStatements()) {
            declaration.getGenerator().generate(compiledScript);
        }

        // save classes
        return compiledScript;
    }

}
