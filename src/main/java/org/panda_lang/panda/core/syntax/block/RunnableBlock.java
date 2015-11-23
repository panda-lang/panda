package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.essential.CustomParser;
import org.panda_lang.panda.core.parser.improved.essential.ParameterParser;
import org.panda_lang.panda.core.parser.improved.essential.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

public class RunnableBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(RunnableBlock.class, "runnable").parser(new CustomParser() {
            @Override
            public Block parse(BlockInfo blockInfo, Block parent,  Block current, Block previous) {
                current = new RunnableBlock();
                current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
                return current;
            }
        }));
    }

    public RunnableBlock() {
        super.setName("RunnableBlock");
    }

}
