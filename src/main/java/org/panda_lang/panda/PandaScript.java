package org.panda_lang.panda;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.core.statement.block.PandaBlock;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript implements Executable {

    private final Panda panda;
    private final Collection<PandaBlock> elements;
    private String workingDirectory;

    public PandaScript(Panda panda) {
        this.panda = panda;
        this.elements = new ArrayList<>();
    }

    public void addPandaBlock(PandaBlock block) {
        this.elements.add(block);
    }

    @Override
    public Inst execute(Alice alice) {
        for (PandaBlock pandaBlock : elements) {
            pandaBlock.execute(alice);
        }
        return null;
    }

    public Inst call(Class<? extends Block> blockType, String name, RuntimeValue... runtimeValues) {
        for (PandaBlock pandaBlock : elements) {
            Inst inst = pandaBlock.call(blockType, name, runtimeValues);
            if (inst != null) {
                return inst;
            }
        }
        return null;
    }

    public Collection<Structure> extractVials() {
        Collection<Structure> structures = new ArrayList<>(1);
        for (PandaBlock pandaBlock : elements) {
            structures.addAll(pandaBlock.extractVials());
        }
        return structures;
    }

    public Collection<PandaBlock> getElements() {
        return elements;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Panda getPanda() {
        return panda;
    }

}
