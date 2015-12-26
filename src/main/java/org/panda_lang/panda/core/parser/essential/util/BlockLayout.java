package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.ElementsPuller;
import org.panda_lang.panda.core.syntax.Block;

public class BlockLayout {

    private final Class<? extends Block> clazz;
    private final String[] indications;
    private final boolean conventional;
    private BlockInitializer parser;

    public BlockLayout(Class<? extends Block> clazz, String... indications) {
        this(clazz, true, indications);
    }

    public BlockLayout(Class<? extends Block> clazz, boolean conventional, String... indications) {
        this.clazz = clazz;
        this.indications = indications;
        this.conventional = conventional;
        ElementsPuller.registerBlock(this);
    }

    public BlockLayout parser(BlockInitializer parser) {
        this.parser = parser;
        return this;
    }

    public boolean isConventional() {
        return conventional;
    }

    public BlockInitializer getParser() {
        return parser;
    }

    public String[] getIndications() {
        return indications;
    }

    public Class<? extends Block> getClazz() {
        return clazz;
    }

}
