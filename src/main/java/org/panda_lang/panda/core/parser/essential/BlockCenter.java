package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.essential.util.BlockLayout;

import java.util.ArrayList;
import java.util.Collection;

public class BlockCenter {

    private final Collection<BlockLayout> blocks = new ArrayList<>();

    public void registerBlock(BlockLayout scheme) {
        blocks.add(scheme);
    }

    public Collection<BlockLayout> getBlocks() {
        return blocks;
    }

}
