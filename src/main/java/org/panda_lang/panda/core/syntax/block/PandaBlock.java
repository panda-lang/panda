package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.GlobalVariables;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Variable;

import java.util.ArrayList;
import java.util.Collection;

public class PandaBlock extends Block {

    private final Collection<Variable> variables;

    public PandaBlock() {
        super.setName("Panda Block");
        this.variables = new ArrayList<>();
        this.setVariableMap(GlobalVariables.VARIABLES);
    }

    public void initVariables() {
        for(Variable variable : variables) {
            variable.run();
        }
    }

    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

}
