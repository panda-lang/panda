package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.CustomParser;
import org.panda_lang.panda.core.parser.ParameterParser;
import org.panda_lang.panda.core.parser.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PObject;

public class WhileBlock extends Block {

    static {
        new BlockScheme(WhileBlock.class, "while").parser(new CustomParser<Block>() {
            @Override
            public Block parse(BlockInfo blockInfo, Block current, Block latest) {
                current = new WhileBlock();
                current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
                return current;
            }
        });
    }

    public WhileBlock(){
        super.setName("WhileBlock");
    }

    @Override
    public PObject run(Parameter... vars) {
        while(((PBoolean) parameters[0].getValue()).isTrue()){
            PObject o = super.run(vars);
            if(o != null) return o;
        } return null;
    }

}
