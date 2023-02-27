package panda.interpreter.language.require;

import panda.interpreter.compiler.CompiledScript;
import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.parser.Declaration.InScriptDeclaration;

public class ImportDeclaration implements InScriptDeclaration {

    private final String javaType;

    public ImportDeclaration(String javaType) {
        this.javaType = javaType;
    }

    @Override
    public InScriptGenerator getGenerator() {
        return (CompiledScript compiledScript) -> {
            compiledScript.addImport(javaType);
        };
    }

}
