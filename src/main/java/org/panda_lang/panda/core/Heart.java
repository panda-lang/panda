package org.panda_lang.panda.core;

import org.panda_lang.panda.core.memory.Cache;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Return;
import org.panda_lang.panda.core.statement.util.ExecutableCell;

public class Heart implements Executable {

    private final Block block;

    public Heart(Block block) {
        this.block = block;
    }

    @Override
    public Essence run(Alice alice) {
        Memory memory = alice.getMemory();
        Cache cache = memory.getCache();

        // todo: Parameters

        for (ExecutableCell executableCell : block.getExecutableCells()) {
            Essence result;
            Executable executable = executableCell.getExecutable();

            if (executable instanceof Block) {
                Memory blockMemory = new Memory(memory);
                blockMemory.setCache(cache);
                Alice blockAlice = new Alice()
                        .fork()
                        .memory(blockMemory);
                blockMemory.setBlock((Block) executable);
                result = executable.run(blockAlice);
            }
            else if (executable instanceof Return) {
                result = executable.run(alice);
                cache.proceed(false);
            }
            else if (executable != null) {
                result = executable.run(alice);
            }
            else {
                result = null;
            }

            // todo: Return
        }

        return null;
    }

    public Block getBlock() {
        return block;
    }

}
