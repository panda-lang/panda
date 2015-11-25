package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.lang.PObject;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript {

    private String name;
    private String author;
    private String version;
    private Collection<Block> blocks;

    public PandaScript() {
        this.blocks = new ArrayList<>();
    }

    public PandaScript name(String name) {
        this.name = name;
        return this;
    }

    public PandaScript author(String author) {
        this.author = author;
        return this;
    }

    public PandaScript version(String version) {
        this.version = version;
        return this;
    }

    public PandaScript sections(Collection<Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public void addSection(Block block) {
        this.blocks.add(block);
    }

    public PObject call(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (Block block : blocks) {
            if (block.getClass() == blockType && block.getName().equals(name)) {
                return block.run(parameters);
            }
        }
        return null;
    }

    public void callAll(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (Block block : blocks) {
            if (block.getClass() == blockType && block.getName().equals(name)) {
                block.run(parameters);
            }
        }
    }

}
