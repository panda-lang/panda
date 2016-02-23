package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Global;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.*;

import java.util.ArrayList;
import java.util.Collection;

public class PandaBlock extends Block {

    private final PandaScript pandaScript;
    private final Collection<Library> libraries;
    private final Collection<Import> imports;
    private final Memory memory;
    private Group group;

    public PandaBlock(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
        this.memory = new Memory(Global.COMMON_MEMORY);
        this.libraries = new ArrayList<>(0);
        this.imports = new ArrayList<>();
        super.setName("Panda Block");
    }

    public void initializeGlobalVariables() {
        Particle particle = new Particle()
                .pandaScript(pandaScript)
                .memory(memory);

        for (NamedExecutable executable : getExecutables()) {
            if (executable instanceof Field) {
                executable.run(particle);
            }
        }
    }

    @Override
    public Essence run(Particle particle) {
        for (NamedExecutable namedExecutable : getExecutables()) {
            if (namedExecutable instanceof Block) {
                continue;
            }
            namedExecutable.run(particle);
        }
        return null;
    }

    public Essence call(Class<? extends Block> blockType, String name, Factor... factors) {
        for (NamedExecutable executable : super.getExecutables()) {
            if (executable.getClass() == blockType && executable.getName().equals(name)) {
                Particle particle = new Particle()
                        .pandaScript(pandaScript)
                        .memory(memory)
                        .factors(factors);
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

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean isReturned() {
        return true;
    }

    public Collection<Library> getLibraries() {
        return libraries;
    }

    public Collection<Import> getImports() {
        return imports;
    }

    public Group getGroup() {
        return group;
    }

}
