package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.*;

public class MethodBlock extends Block {

    static {
        BlockCenter.registerBlock(new BlockLayout(MethodBlock.class, "method", "function", "constructor", "public").initializer(new BlockInitializer() {
            @Override
            public MethodBlock initialize(Atom atom) {
                MethodBlock block = new MethodBlock(atom.getBlockInfo().getSpecifiers().get(0));
                block.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return block;
            }
        }));
    }

    public MethodBlock(String name) {
        super.setName(name);
    }

    @Override
    public Essence run(Particle particle) {
        Factor[] vars = particle.getFactors();
        if (factors != null && (vars == null || vars.length != factors.length)) {
            System.out.println("[MethodBlock] " + getName() + ": Bad factors!");
            return null;
        }
        return super.run(particle);
    }

    public Method toMethod() {
        final MethodBlock methodBlock = this;
        return new Method(new NamedExecutable() {
            @Override
            public String getName() {
                return methodBlock.getName();
            }

            @Override
            public Essence run(Particle particle) {
                return methodBlock.run(particle);
            }
        });
    }

}
