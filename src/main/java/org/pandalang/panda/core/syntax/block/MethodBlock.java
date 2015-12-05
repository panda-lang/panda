package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.ParameterParser;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.lang.PObject;

public class MethodBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(MethodBlock.class, "method", "function").parser(new BlockInitializer() {
            @Override
            public MethodBlock initialize(Atom atom) {
                MethodBlock block = new MethodBlock(atom.getBlockInfo().getSpecifiers().get(0));
                block.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return block;
            }
        }));
    }

    public MethodBlock(String name) {
        super.setName(name);
    }

    @Override
    public PObject run(Parameter... vars) {
        if (parameters != null && (vars == null || vars.length != parameters.length)) {
            System.out.println("[MethodBlock] " + getName() + ": Bad parameters!");
            return null;
        }
        return super.run(vars);
    }

}
