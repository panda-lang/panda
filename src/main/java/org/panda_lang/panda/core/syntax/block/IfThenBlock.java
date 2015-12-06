package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;

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
    public Essence run(Particle particle) {
        Essence object = particle.get(0).getValue();

        /*
        if (object instanceof PBoolean) {
            PBoolean b = (PBoolean) object;

            if (b.isTrue()) {
                return super.run(vars);
            } else if (elseThenBlock != null) {
                return elseThenBlock.run(vars);
            }
        }
        */
        return null;
    }

    public void setElseThenBlock(Block block) {
        this.elseThenBlock = block;
    }

}
