package org.pandalang.panda.core.scheme;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.syntax.Block;

public class BlockScheme {

    private final Class<? extends Block> clazz;
    private final String[] indications;
    private final boolean conventional;
    private BlockInitializer parser;

    public BlockScheme(Class<? extends Block> clazz, String... indications) {
        this(clazz, true, indications);
    }

    public BlockScheme(Class<? extends Block> clazz, boolean conventional, String... indications) {
        this.clazz = clazz;
        this.indications = indications;
        this.conventional = conventional;
        ElementsBucket.registerBlock(this);
    }

    public BlockScheme parser(BlockInitializer parser) {
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
