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

public class WhileBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(WhileBlock.class, "while").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new WhileBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    public WhileBlock() {
        super.setName("while::" + System.nanoTime());
    }

    @Override
    public PObject run(Parameter... vars) {
        while (parameters[0].getValue(PBoolean.class).isTrue()) {
            PObject o = super.run(vars);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

}
