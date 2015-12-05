package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.ParameterParser;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.lang.PNumber;
import org.pandalang.panda.lang.PObject;

public class ForBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ForBlock.class, "for", "loop").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ForBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    public ForBlock() {
        super.setName("for::" + System.nanoTime());
    }

    @Override
    public PObject run(Parameter... vars) {
        PObject object = parameters[0].getValue();
        if (object instanceof PNumber) {
            Number times = ((PNumber) object).getNumber();
            for (int i = 0; i < times.intValue(); i++) {
                PObject o = super.run(vars);
                if (o != null) {
                    return o;
                }
            }
        }
        return null;
    }

}
