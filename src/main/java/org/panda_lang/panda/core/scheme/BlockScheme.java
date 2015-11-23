package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.parser.depracted.CustomParser;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.ElementsBucket;

public class BlockScheme {

    private final Class<? extends Block> clazz;
    private final String[] indications;
    private final boolean conventional;
    private CustomParser<? extends Block> parser;

    public BlockScheme(Class<? extends Block> clazz, String... indications){
        this(clazz, true, indications);
    }
    
    public BlockScheme(Class<? extends Block> clazz, boolean conventional, String... indications){
        this.clazz = clazz;
        this.indications = indications;
        this.conventional = conventional;
        ElementsBucket.registerBlock(this);
    }

    public void parser(CustomParser<? extends Block> parser){
        this.parser = parser;
    }

    public boolean isConventional() {
        return conventional;
    }

    public CustomParser<? extends Block> getParser(){
        return parser;
    }
    
    public String[] getIndications() {
        return indications;
    }
    
    public Class<? extends Block> getClazz(){
        return clazz;
    }

}
