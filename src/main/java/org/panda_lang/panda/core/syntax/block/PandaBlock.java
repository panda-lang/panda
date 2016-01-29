package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.Global;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.*;

import java.util.ArrayList;
import java.util.Collection;

public class PandaBlock extends Block {

    private final Collection<Import> imports;
    private final Memory memory;
    private Group group;

    public PandaBlock() {
        super.setName("Panda Block");
        this.memory = new Memory(Global.COMMON_MEMORY);
        this.imports = new ArrayList<>();
    }

    public void initializeGlobalVariables() {
        Particle particle = new Particle(memory);
        for (NamedExecutable executable : getExecutables()) {
            if (executable instanceof Field) {
                executable.run(particle);
            }
        }
    }

    public Essence call(Class<? extends Block> blockType, String name, Factor... factors) {
        for (NamedExecutable executable : super.getExecutables()) {
            if (executable.getClass() == blockType && executable.getName().equals(name)) {
                Particle particle = new Particle(memory);
                particle.setFactors(factors);
                return executable.run(particle);
            }
        }
        return null;
    }

    public Collection<Vial> extractVials() {
        Collection<Vial> vials = new ArrayList<>(1);
        for (NamedExecutable executable : super.getExecutables()) {
            if (executable instanceof VialBlock) {
                Vial vial = ((VialBlock) executable).getVial();
                vials.add(vial);
            }
        }
        return vials;
    }

    @Override
    public boolean isReturned() {
        return true;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

}
