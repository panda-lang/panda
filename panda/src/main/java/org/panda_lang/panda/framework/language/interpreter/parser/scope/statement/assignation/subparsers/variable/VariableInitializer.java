package org.panda_lang.panda.framework.language.interpreter.parser.scope.statement.assignation.subparsers.variable;

import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;

public class VariableInitializer {

    public static final String DECLARATION_PARSER = "mutable:[mutable] nullable:[nullable] <type> <name:condition token {type:unknown}>";

    public Variable createVariable(ModuleLoader loader, Scope scope, boolean mutable, boolean nullable, String type, String name) {
        ClassPrototype prototype = loader.forClass(type);

        Variable variable = new PandaVariable(prototype, name, 0, mutable, nullable);
        scope.addVariable(variable);

        return variable;
    }

}
