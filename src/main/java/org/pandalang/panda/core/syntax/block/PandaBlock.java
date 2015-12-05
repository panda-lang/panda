package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.GlobalVariables;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Executable;
import org.pandalang.panda.core.syntax.Variable;

public class PandaBlock extends Block {

    public PandaBlock() {
        super.setName("Panda Block");
        super.setVariableMap(GlobalVariables.VARIABLES);
    }

    public void initializeGlobalVariables() {
        for (Executable executable : getExecutables()) {
            if(executable instanceof Variable) {
                executable.run();
            }
        }
    }

}
