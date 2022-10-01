package panda.interpreter.parser;

import panda.interpreter.compiler.Generator;
import panda.interpreter.compiler.Generator.InBodyGenerator;
import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.compiler.Generator.InTypeGenerator;

public interface Declaration<GENERATOR extends Generator> {

    interface InScriptDeclaration extends Declaration<InScriptGenerator> {

        @Override
        default Scope getScope() {
            return Scope.SCRIPT;
        }

    }

    interface InTypeDeclaration extends Declaration<InTypeGenerator> {

        @Override
        default Scope getScope() {
            return Scope.TYPE;
        }

    }

    interface InBodyDeclaration extends Declaration<InBodyGenerator> {

        @Override
        default Scope getScope() {
            return Scope.FUNCTION;
        }

    }

    GENERATOR getGenerator();

    Scope getScope();

}
