package org.pandalang.panda;

import org.pandalang.panda.core.syntax.Block;
import org.pandalang.panda.core.syntax.Executable;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.core.syntax.block.PandaBlock;
import org.pandalang.panda.lang.PObject;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript {

    private String name;
    private String author;
    private String version;
    private Collection<PandaBlock> blocks;

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

    public PandaScript sections(Collection<PandaBlock> blocks) {
        this.blocks = blocks;
        return this;
    }

    public void addPandaBlock(PandaBlock block) {
        this.blocks.add(block);
    }

    public PObject call(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (PandaBlock pandaBlock : blocks) {
            for (Executable executable : pandaBlock.getExecutables()) {
                if (executable.getClass() == blockType && executable.getName().equals(name)) {
                    //System.out.println("----------------------- CONSOLE");
                    return executable.run(parameters);
                }
            }
        }
        return null;
    }

    public void callAll(Class<? extends Block> blockType, String name, Parameter... parameters) {
        for (PandaBlock pandaBlock : blocks) {
            for (Executable executable : pandaBlock.getExecutables()) {
                if (executable.getClass() == blockType && executable.getName().equals(name)) {
                    executable.run(parameters);
                }
            }
        }
    }

}
