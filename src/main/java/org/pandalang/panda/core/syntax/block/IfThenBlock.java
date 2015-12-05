package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.ParameterParser;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.lang.PBoolean;
import org.pandalang.panda.lang.PObject;

public class IfThenBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(IfThenBlock.class, "if").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new IfThenBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    private Block elseThenBlock;

    public IfThenBlock() {
        super.setName("if-then::" + System.nanoTime());
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
