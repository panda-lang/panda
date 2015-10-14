package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.CustomParser;
import org.panda_lang.panda.core.parser.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    static {
        new BlockScheme(ElseThenBlock.class, false, "else").parser(new CustomParser<Block>() {
            @Override
            public Block parse(BlockInfo blockInfo, Block current, Block latest) {
                current = new ElseThenBlock();
                if(latest instanceof IfThenBlock){
                    ((IfThenBlock) latest).setElseThenBlock(current);
                }
                return current;
            }
        });
    }
    
    public ElseThenBlock(){
        super.setName("ElseThenBlock");
    }

}
