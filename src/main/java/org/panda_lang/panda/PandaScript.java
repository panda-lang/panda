package org.panda_lang.panda;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

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
    public Essence run(Particle particle) {
        for (PandaBlock pandaBlock : elements) {
            pandaBlock.run(particle);
        }
        return null;
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

    public Collection<PandaBlock> getElements() {
        return elements;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public Panda getPanda() {
        return panda;
    }

}
