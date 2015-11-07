package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.CustomParser;
import org.panda_lang.panda.core.parser.ParameterParser;
import org.panda_lang.panda.core.parser.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PObject;

public class ForBlock extends Block {

    static {
        new BlockScheme(ForBlock.class, "for", "loop").parser(new CustomParser<Block>() {
            @Override
            public Block parse(BlockInfo blockInfo, Block current, Block latest) {
                current = new ForBlock();
                current.setParameters(new ParameterParser().parse(current, blockInfo.getParameters()));
                return current;
            }
        });
    }

    public ForBlock(){
        super.setName("ForBlock");
    }

    @Override
    public PObject run(Parameter... vars) {
        PObject object = parameters[0].getValue();
        if(object instanceof PNumber){
            Number times = ((PNumber) object).getNumber();
            for(int i = 0; i < times.intValue(); i++){
                PObject o = super.run(vars);
                if(o != null) return o;
            }
        }
        return null;
    }

}
