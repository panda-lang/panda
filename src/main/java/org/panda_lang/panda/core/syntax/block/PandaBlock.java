package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.GlobalVariables;
import org.panda_lang.panda.core.syntax.Block;

public class PandaBlock extends Block {

    public PandaBlock() {
        super.setName("Panda Block");
        super.setVariableMap(GlobalVariables.VARIABLES);
    }

}
