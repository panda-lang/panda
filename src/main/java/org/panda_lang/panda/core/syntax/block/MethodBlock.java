package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.depracted.CustomParser;
import org.panda_lang.panda.core.parser.depracted.ParameterParser;
import org.panda_lang.panda.core.parser.depracted.util.BlockInfo;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PObject;

public class MethodBlock extends Block {

    static {
        new BlockScheme(MethodBlock.class, "method").parser(new CustomParser<MethodBlock>(){
            @Override
            public MethodBlock parse(BlockInfo blockInfo, Block current, Block latest) {
                MethodBlock block = new MethodBlock(blockInfo.getSpecifiers().get(0));
                block.setParameters(new ParameterParser().parse(block, blockInfo.getParameters()));
                return block;
            }
        });
    }

    public MethodBlock(String name) {
        super.setName(name);
    }

    @Override
    public PObject run(Parameter... vars) {
        if(parameters != null && (vars == null || vars.length != parameters.length)){
            System.out.println("[MethodBlock] " + getName() +": Bad parameters!");
            return null;
        }
        return super.run(vars);
    }

}
