package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.depracted.CustomParser;
import org.panda_lang.panda.core.parser.depracted.ParameterParser;
import org.panda_lang.panda.core.parser.depracted.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

public class ClassBlock extends Block {
    
    static {
        new BlockScheme(ClassBlock.class, false, "class").parser(new CustomParser<Block>() {
            @Override
            public Block parse(BlockInfo blockInfo, Block current, Block latest) {
                current = new ClassBlock();
                current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
                return current;
            }
        });
    }

}
