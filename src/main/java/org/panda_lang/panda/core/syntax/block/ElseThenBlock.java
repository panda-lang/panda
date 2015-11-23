package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.essential.CustomParser;
import org.panda_lang.panda.core.parser.improved.essential.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ElseThenBlock.class, false, "else").parser(new CustomParser() {
            @Override
            public Block parse(BlockInfo blockInfo, Block parent, Block current, Block previous) {
                current = new ElseThenBlock();
                if(previous instanceof IfThenBlock){
                    ((IfThenBlock) previous).setElseThenBlock(current);
                }
                return current;
            }
        }));
    }
    
    public ElseThenBlock(){
        super.setName("ElseThenBlock");
    }

}
