package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.essential.CustomParser;
import org.panda_lang.panda.core.parser.improved.essential.ParameterParser;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PObject;

public class MethodBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(MethodBlock.class, "method", "function").parser(new CustomParser() {
            @Override
            public MethodBlock parse(Atom atom) {
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
