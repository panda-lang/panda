package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PObject;
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

    public PObject start(final Parameter... vars) {
        final Block block = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Executable executable : block.getExecutables()) {
                    executable.run(vars);
                }
            }
        });
        if (parameters != null && parameters.length > 0) {
            PObject value = parameters[0].getValue();
            thread.setName(pThread.getName());
        }
        thread.start();
        return null;
    }

    @Override
    public PObject run(final Parameter... vars) {
        if (parameters.length == 0) {
            start(vars);
            return null;
        } else {
            PObject value = parameters[0].getValue();
            if (value instanceof PThread) {
                pThread = (PThread) value;
                pThread.setBlock(this);
            }
            return value;
        }
    }

}
