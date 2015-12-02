package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.block.PandaBlock;
import org.panda_lang.panda.lang.PObject;

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
        System.out.println("------------------ call('" + name + "'): ");
        for (PandaBlock pandaBlock : blocks) {
            for (Executable executable : pandaBlock.getExecutables()) {
                System.out.println(executable.getName());
                if (executable.getClass() == blockType && executable.getName().equals(name)) {
                    System.out.println('\n' + "------------------ console():");
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
