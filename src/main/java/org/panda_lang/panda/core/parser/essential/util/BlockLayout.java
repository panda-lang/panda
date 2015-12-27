package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.syntax.Block;

public class BlockLayout {

    private final Class<? extends Block> clazz;
    private final String[] indications;
    private final boolean conventional;
    private BlockInitializer initializer;

    public BlockLayout(Class<? extends Block> clazz, String... indications) {
        this(clazz, true, indications);
    }

    public BlockLayout(Class<? extends Block> clazz, boolean conventional, String... indications) {
        this.clazz = clazz;
        this.indications = indications;
        this.conventional = conventional;
        BlockCenter.registerBlock(this);
    }

    public BlockLayout initializer(BlockInitializer initializer) {
        this.initializer = initializer;
        return this;
    }

    public boolean isConventional() {
        return conventional;
    }

    public BlockInitializer getInitializer() {
        return initializer;
    }

    public String[] getIndications() {
        return indications;
    }

    public Class<? extends Block> getClazz() {
        return clazz;
    }

}
