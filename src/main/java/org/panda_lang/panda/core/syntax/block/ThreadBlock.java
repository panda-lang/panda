package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PThread;

public class ThreadBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ThreadBlock.class, "thread").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ThreadBlock();
                Parameter[] parameters = new ParameterParser().parse(atom, atom.getBlockInfo().getParameters());
                current.setParameters(parameters);
                return current;
            }
        }));
    }

    private PThread pThread;

    public ThreadBlock() {
        super.setName("thread::" + System.nanoTime());
    }

    public Essence start(final Particle particle) {
        final Block block = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (NamedExecutable executable : block.getExecutables()) {
                    executable.run(particle);
                }
            }
        });
        if (parameters != null && parameters.length > 0) {
            Essence value = parameters[0].getValue();
            thread.setName(pThread.getName());
        }
        thread.start();
        return null;
    }

    @Override
    public Essence run(final Particle particle) {
        if (parameters.length == 0) {
            start(particle);
            return null;
        } else {
            pThread = parameters[0].getValue(PThread.class);
            pThread.setBlock(this);
            return pThread;
        }
    }

}
