package org.panda_lang.panda.language.interpreter.parsers.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;

public class BlockComponents {

    public static final Component<Block> BLOCK = Component.of("block", Block.class);

    public static final Component<Block> PREVIOUS_BLOCK = Component.of("previous-block", Block.class);

    public static final Component<Boolean> UNLISTED_BLOCK = Component.of("unlisted-block", Boolean.class);

}
