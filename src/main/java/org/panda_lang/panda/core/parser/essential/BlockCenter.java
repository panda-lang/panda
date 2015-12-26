package org.panda_lang.panda.core.parser.essential;

import org.panda_lang.panda.core.parser.essential.util.BlockLayout;

import java.util.ArrayList;
import java.util.Collection;

public class BlockCenter {

    private final static Collection<BlockLayout> blocks = new ArrayList<>();

    public static void registerBlock(BlockLayout scheme) {
        blocks.add(scheme);
    }

    public static Collection<BlockLayout> getBlocks() {
        return blocks;
    }

}
