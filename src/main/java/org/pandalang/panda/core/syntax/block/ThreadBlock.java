package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.ParameterParser;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Executable;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.lang.PObject;
import org.pandalang.panda.lang.PThread;

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
