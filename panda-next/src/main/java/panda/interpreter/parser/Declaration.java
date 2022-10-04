package panda.interpreter.parser;

import panda.interpreter.compiler.Generator.InBodyGenerator;
import panda.interpreter.compiler.Generator.InScriptGenerator;
import panda.interpreter.compiler.Generator.InTypeGenerator;

public interface Declaration {

    interface InScriptDeclaration extends Declaration {

        @Override
        default Scope getScope() {
            return Scope.SCRIPT;
        }

        InScriptGenerator getGenerator();
    }

    interface InTypeDeclaration extends Declaration {

        @Override
        default Scope getScope() {
            return Scope.TYPE;
        }

        InTypeGenerator getGenerator();

    }

    interface InBodyDeclaration extends Declaration {

        @Override
        default Scope getScope() {
            return Scope.BODY;
        }

        InBodyGenerator getGenerator();

    }

    Scope getScope();

}
