package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Vial;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript {

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

    public Essence call(Class<? extends Block> blockType, String name, Factor... factors) {
        for (PandaBlock pandaBlock : elements) {
            Essence essence = pandaBlock.call(blockType, name, factors);
            if (essence != null) {
                return essence;
            }
        }
        return null;
    }

    public Collection<Vial> extractVials() {
        Collection<Vial> vials = new ArrayList<>(1);
        for (PandaBlock pandaBlock : elements) {
            vials.addAll(pandaBlock.extractVials());
        }
        return vials;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Panda getPanda() {
        return panda;
    }

}
