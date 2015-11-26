package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.essential.CustomParser;
import org.panda_lang.panda.core.parser.improved.essential.ParameterParser;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PObject;

public class IfThenBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(IfThenBlock.class, "if").parser(new CustomParser() {
            @Override
            public Block parse(Atom atom) {
                Block current = new IfThenBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    private Block elseThenBlock;

    public IfThenBlock() {
        super.setName("IfThenBlock");
    }

    @Override
    public PObject run(Parameter... vars) {
        PObject object = parameters[0].getValue();

        if (object instanceof PBoolean) {
            PBoolean b = (PBoolean) object;

            if (b.isTrue()) {
                return super.run(vars);
            } else if (elseThenBlock != null) {
                return elseThenBlock.run(vars);
            }
        }
        return null;
    }

    public void setElseThenBlock(Block block) {
        this.elseThenBlock = block;
    }

}
