package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.GlobalVariables;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.Variable;

public class PandaBlock extends Block {

    public PandaBlock() {
        super.setName("Panda Block");
        super.setVariableMap(GlobalVariables.VARIABLES);
    }

    public void initializeGlobalVariables() {
        for (NamedExecutable executable : getExecutables()) {
            if (executable instanceof Variable) {
                executable.run(null);
            }
        }
    }

}
